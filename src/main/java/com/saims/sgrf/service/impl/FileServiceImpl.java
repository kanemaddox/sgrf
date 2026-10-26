package com.saims.sgrf.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.saims.sgrf.service.FileService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

	@Value("${app.upload.dir}")
	private String uploadDir;

	@Transactional
	public String createProfile(MultipartFile file) throws IOException {

		if (file.isEmpty())
			throw new RuntimeException("Archivo vacío");

		// 1. Validar tipo y extensión
		String contentType = file.getContentType();
		String extension = "jpg"; // default
		if ("image/png".equals(contentType))
			extension = "png";
		else if (!"image/jpeg".equals(contentType))
			throw new RuntimeException("Tipo no permitido");

		// 2. Definir nombre único para el archivo (será el mismo en todas las carpetas)
		String nombreArchivo = System.currentTimeMillis() + "." + extension;

		// 3. Tamaños a procesar
		int[] anchos = { 800, 200, 100, 50 };

		// 4. Directorio base de usuarios
		Path rutaBase = Paths.get(this.uploadDir).resolve("usuarios");

		for (int ancho : anchos) {
			// Creamos la ruta de la subcarpeta específica: ej. "uploads/usuarios/200"
			Path carpetaDimension = rutaBase.resolve(String.valueOf(ancho));

			// Crear la carpeta si no existe
			if (!Files.exists(carpetaDimension)) {
				Files.createDirectories(carpetaDimension);
			}

			// Definir la ruta completa del archivo final: ej.
			// "uploads/usuarios/200/12345.jpg"
			Path rutaDestino = carpetaDimension.resolve(nombreArchivo);

			// Procesar y guardar con Thumbnailator
			Thumbnails.of(file.getInputStream()).size(ancho, ancho).crop(Positions.CENTER).keepAspectRatio(true)
					.outputFormat(extension).toFile(rutaDestino.toFile());
		}

		// Retornamos solo el nombre del archivo
		return nombreArchivo;
	}

	@Transactional
	public String createDocument(MultipartFile file) throws IOException {

		if (file.isEmpty())
			throw new RuntimeException("Archivo vacío");

		String contentType = file.getContentType();
		String baseName = String.valueOf(System.currentTimeMillis());
		Path rutaBase = Paths.get(this.uploadDir).resolve("document");
		
		Path rutaImage = Paths.get(this.uploadDir).resolve("document\\image");
		
		//Path rutaImage = Paths.get(this.uploadDir).resolve("document/image");
		
		int[] anchos = { 1020, 800, 200, 100, 50 };

		if ("application/pdf".equals(contentType)) {
			// =================================================================
			// PIPELINE A: PDF -> Guardar Original -> Convertir a JPG -> Tamaños
			// =================================================================

			// 1. Guardar el PDF original
			Path carpetaPdf = rutaBase.resolve("pdf");
			Files.createDirectories(carpetaPdf);
			Path rutaPdfOriginal = carpetaPdf.resolve(baseName + ".pdf");
			file.transferTo(rutaPdfOriginal.toFile());

			// 2. Renderizar la primera página del PDF a BufferedImage
			try (PDDocument document = Loader.loadPDF(rutaPdfOriginal.toFile())) {
				PDFRenderer pdfRenderer = new PDFRenderer(document);
				// Renderiza la página 0 a 300 DPI para buena calidad de conversión
				BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300);

				// 3. Procesar y guardar en los diferentes tamaños (como JPG)
				procesarYGuardarImagenesPdf(bufferedImage, anchos, baseName, "jpg", rutaImage);
			}

			return baseName;

		} else if ("image/jpeg".equals(contentType) || "image/png".equals(contentType)) {

			// =================================================================
			// PIPELINE B: Imagen -> Tamaños -> Convertir a PDF -> Guardar PDF
			// =================================================================
			String extension = "jpg";
			
			byte[] bytesImagen = file.getBytes();
			
			// 1. Procesar y guardar en las subcarpetas de tamaños
			procesarYGuardarImagenes(file, anchos, baseName, extension, rutaImage);

			// 2. Convertir la imagen original a PDF y guardarla
			Path carpetaPdfResultante = rutaBase.resolve("pdf");
			Files.createDirectories(carpetaPdfResultante);
			Path rutaPdfDestino = carpetaPdfResultante.resolve(baseName + ".pdf");

			try (InputStream is = new ByteArrayInputStream(bytesImagen)) {
				convertirImagenAPdf(is, rutaPdfDestino);
			}
			return baseName;

		} else {
			throw new RuntimeException("Tipo de archivo no permitido (Solo JPG, PNG o PDF)");
		}
	}

	/**
	 * Método helper para redimensionar y guardar imágenes usando Thumbnailator
	 * desde un InputStream
	 */
	private void procesarYGuardarImagenes(MultipartFile file, int[] anchos, String baseName, String extension, Path rutaBase)
			throws IOException {

		for (int ancho : anchos) {
			Path carpetaDimension = rutaBase.resolve(String.valueOf(ancho));
			Files.createDirectories(carpetaDimension);
			Path rutaDestino = carpetaDimension.resolve(baseName + "." + extension);

			Thumbnails.of(file.getInputStream()).size(ancho, ancho).keepAspectRatio(true)
					.outputFormat(extension).toFile(rutaDestino.toFile());
		}
	}

	/**
	 * Método helper para envolver una imagen dentro de un documento PDF de su mismo
	 * tamaño
	 */
	private void convertirImagenAPdf(InputStream imagenStream, Path rutaPdfDestino) throws IOException {
		try (PDDocument document = new PDDocument()) {
			// Thumbnailator nos ayuda a leer el stream fácilmente
			BufferedImage bimg = Thumbnails.of(imagenStream).scale(1.0).asBufferedImage();

			// Crear una página del mismo tamaño en puntos que la imagen (1 px = 1 punto
			// base)
			PDPage page = new PDPage(new PDRectangle(bimg.getWidth(), bimg.getHeight()));
			document.addPage(page);

			// Crear el objeto de imagen para PDFBox (convertido a JPEG internamente para
			// comprimir)
			PDImageXObject pdImage = JPEGFactory.createFromImage(document, bimg);

			// Dibujar la imagen en el PDF
			try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
				contentStream.drawImage(pdImage, 0, 0, bimg.getWidth(), bimg.getHeight());
			}

			document.save(rutaPdfDestino.toFile());
		}
	}
	
	private void procesarYGuardarImagenesPdf(Object source, int[] anchos, String baseName, String extension, Path rutaBase) throws IOException {
	    for (int ancho : anchos) {
	        Path carpetaDimension = rutaBase.resolve(String.valueOf(ancho));
	        Files.createDirectories(carpetaDimension);
	        Path rutaDestino = carpetaDimension.resolve(baseName + "." + extension);

	        var builder = (source instanceof BufferedImage) ? 
	                Thumbnails.of((BufferedImage) source) : Thumbnails.of((InputStream) source);

	        builder.size(ancho, ancho)
	               .keepAspectRatio(true)
	               .outputFormat(extension)
	               .toFile(rutaDestino.toFile());
	    }
	}
}
