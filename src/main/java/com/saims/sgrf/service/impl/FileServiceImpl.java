package com.saims.sgrf.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.saims.sgrf.service.FileService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{

    @Value("${app.upload.dir}")
    private String uploadDir;
        
    @Transactional
    public String createProfile(MultipartFile file) throws IOException {
    	
    	if (file.isEmpty()) throw new RuntimeException("Archivo vacío");

        // 1. Validar tipo y extensión
        String contentType = file.getContentType();
        String extension = "jpg"; // default
        if ("image/png".equals(contentType)) extension = "png";
        else if (!"image/jpeg".equals(contentType)) throw new RuntimeException("Tipo no permitido");

        // 2. Definir nombre único para el archivo (será el mismo en todas las carpetas)
        String nombreArchivo = System.currentTimeMillis() + "." + extension;

        // 3. Tamaños a procesar
        int[] anchos = {800, 200, 100};

        // 4. Directorio base de usuarios
        Path rutaBase = Paths.get(this.uploadDir).resolve("usuarios");

        for (int ancho : anchos) {
            // Creamos la ruta de la subcarpeta específica: ej. "uploads/usuarios/200"
            Path carpetaDimension = rutaBase.resolve(String.valueOf(ancho));
            
            // Crear la carpeta si no existe
            if (!Files.exists(carpetaDimension)) {
                Files.createDirectories(carpetaDimension);
            }

            // Definir la ruta completa del archivo final: ej. "uploads/usuarios/200/12345.jpg"
            Path rutaDestino = carpetaDimension.resolve(nombreArchivo);

            // Procesar y guardar con Thumbnailator
            Thumbnails.of(file.getInputStream())
                    .width(ancho)
                    .keepAspectRatio(true)
                    .outputFormat(extension)
                    .toFile(rutaDestino.toFile());
        }

        // Retornamos solo el nombre del archivo
        return nombreArchivo;
    }
}
