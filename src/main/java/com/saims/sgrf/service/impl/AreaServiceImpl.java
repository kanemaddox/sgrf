package com.saims.sgrf.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.AreaDao;
import com.saims.sgrf.dto.AreaDto;
import com.saims.sgrf.dto.DatosDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.enums.TipoArea;
import com.saims.sgrf.model.AreaModel;
import com.saims.sgrf.model.DepartamentoModel;
import com.saims.sgrf.service.AreaService;
import com.saims.sgrf.service.DepartamentoService;
import com.saims.sgrf.service.NivelService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService {

	private final AreaDao areaDao;
	private final ModelMapper modelMapper;
	private final NivelService nivelService;
	private final DepartamentoService departamentoService;

	private final Personalizer personalizer = new Personalizer();

	// =========================
	// 🟢 CREATE
	// =========================

	/**
	 * 📌 Crea un área evitando duplicados
	 */
	@Transactional
	public AreaDto.Response createArea(AreaDto.Request request) {

		String nombre = this.personalizer.normalizer(request.getNombre());

		if (existsByNombre(nombre)) {
			throw new RuntimeException("El área ya existe");
		}

		AreaModel area = new AreaModel();
		// area.setNombre(nombre);
		area.setEstado(true);

		this.areaDao.save(area);

		return this.modelToResponse(area);
	}

	/**
	 * 📌 Uso interno o cargas masivas
	 */
	@Transactional
	public AreaModel createOrGet(DatosDto.Request request) {

		/**
		 * para el codigo del centro de costos se toma los 5 primeros dijitos para el
		 * prefijo
		 */

		String codigo = this.personalizer.normalizer(request.getCodigoSubCentroCostos());

		AreaModel centroCosto = this.areaDao.findByCodigo(codigo).orElseGet(() -> {
			AreaModel nuevo = new AreaModel();
			nuevo.setId(0L);
			return nuevo;
		});
		if(centroCosto.getId().longValue()==0L)
			throw new org.springframework.web.server.ResponseStatusException(
			        org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, 
			        "El ID del Centro de Costo no puede ser 0"
			    );
		else
			return centroCosto;
	}

	@Transactional
	public AreaDto.Response createDatos(AreaDto.Request request) {

		String dep = this.personalizer.normalizer(request.getNombre());
		DepartamentoModel departamento = this.departamentoService.createOrGet(dep);
		List<AreaModel> lista = this.areaDao.findAll();
		List<AreaModel> padre = new ArrayList<AreaModel>();
		boolean existe = false;
		int nivel = 0;
		TipoArea tipo = TipoArea.GERENCIA;
		switch (request.getNivel()) {
		case 0:
			nivel = 1;
			tipo = TipoArea.GERENCIA;
			break;
		case 1:
			tipo = TipoArea.SUB_GERENCIA;
			nivel = 2;
			break;
		case 2:
			tipo = TipoArea.CENTRO_DE_COSTOS;
			nivel = 5;
			for (AreaModel value : lista) {
				if (value.getNivel() == 1) {
					padre.add(value);
				}
			}
			break;
		case 3:
			tipo = TipoArea.SUB_CENTRO_DE_COSTOS;
			nivel = 8;
			for (AreaModel value : lista) {
				if (value.getNivel() == 2) {
					padre.add(value);
				}
			}
			break;
		}

		String codigo = this.personalizer.normalizer(request.getCodigo());

		for (AreaModel value : lista) {
			if (value.getCodigo().equals(codigo)) {
				existe = true;
				break;
			}
		}

		if (!existe) {
			AreaModel area = new AreaModel();
			area.setCodigo(codigo);
			area.setPrefijo(this.personalizer.generatePrefijo(codigo, nivel));
			area.setNivel(request.getNivel());
			area.setDepartamento(departamento);
			area.setEstado(true);
			area.setTipo(tipo);
			this.areaDao.save(area);
			if (area.getNivel() > 1) {
				for (AreaModel value : padre) {
					if (value.getPrefijo()
							.equals(this.personalizer.generatePrefijo(codigo, value.getPrefijo().length()))) {
						this.nivelService.createOrGet(value, area);
						break;
					}
				}
			}
		}

		return null;
	}

	// =========================
	// 🔵 READ
	// =========================

	/**
	 * 📌 Obtiene un área por ID
	 */
	@Transactional
	public AreaDto.Response getById(Long id) {

		AreaModel area = this.areaDao.findById(id)
				.orElseThrow(() -> new RuntimeException("Área no encontrada con ID: " + id));

		return this.modelToResponse(area);
	}

	@Transactional
	public List<AreaDto.Estructura> getEstructura() {
		List<AreaModel> lista = this.areaDao.findAll();

		Map<Integer, List<AreaModel>> porNivel = lista.stream().collect(Collectors.groupingBy(AreaModel::getNivel));

		List<AreaModel> nivel1 = Stream.concat(porNivel.getOrDefault(0, Collections.emptyList()).stream(),
				porNivel.getOrDefault(1, Collections.emptyList()).stream()).collect(Collectors.toList());
		List<AreaModel> nivel2 = porNivel.getOrDefault(2, Collections.emptyList());
		List<AreaModel> nivel3 = porNivel.getOrDefault(3, Collections.emptyList());

		// 2. Procesar Nivel 1 (Estructuras)
		List<AreaDto.Estructura> areas = nivel1.stream().map(v1 -> {
			AreaDto.Estructura est = new AreaDto.Estructura();
			est.setIdEstructura(v1.getId());
			est.setCodigo(v1.getCodigo());
			est.setDepartamento(v1.getDepartamento().getNombre());
			est.setPrefijo(v1.getPrefijo());
			est.setTipo(v1.getTipo());
			if (v1.getNivel() >= 1) {
				// 3. Procesar Nivel 2 (Costos) que pertenecen a esta Estructura
				List<AreaDto.Costo> costos = nivel2.stream()
						.filter(v2 -> est.getPrefijo()
								.equals(this.personalizer.generatePrefijo(v2.getCodigo(), est.getPrefijo().length())))
						.map(v2 -> {
							AreaDto.Costo costo = new AreaDto.Costo();
							costo.setIdCosto(v2.getId());
							costo.setCodigo(v2.getCodigo());
							costo.setDepartamento(v2.getDepartamento().getNombre());
							costo.setPrefijo(v2.getPrefijo());
							costo.setTipo(v2.getTipo());

							// 4. Procesar Nivel 3 (Subs) que pertenecen a este Costo
							List<AreaDto.Sub> subs = nivel3.stream().filter(v3 -> costo.getPrefijo().equals(
									this.personalizer.generatePrefijo(v3.getCodigo(), costo.getPrefijo().length())))
									.map(v3 -> {
										AreaDto.Sub sub = new AreaDto.Sub();
										sub.setIdCosto(v3.getId());
										sub.setCodigo(v3.getCodigo());
										sub.setDepartamento(v3.getDepartamento().getNombre());
										sub.setPrefijo(v3.getPrefijo());
										sub.setTipo(v3.getTipo());
										return sub;
									}).collect(Collectors.toList());

							costo.setSub(subs);
							return costo;
						}).collect(Collectors.toList());
				est.setCosto(costos);
			} else {
				List<AreaDto.Costo> costos = nivel1.stream().filter(v -> v.getNivel() == 1).map(v -> {
					AreaDto.Costo costo = new AreaDto.Costo();
					costo.setIdCosto(v.getId());
					costo.setCodigo(v.getCodigo());
					costo.setDepartamento(v.getDepartamento().getNombre());
					costo.setPrefijo(v.getPrefijo());
					costo.setTipo(v.getTipo());
					return costo;
				}).collect(Collectors.toList());

				est.setCosto(costos);
			}

			return est;
		}).collect(Collectors.toList());
		
		return areas;
	}

	/**
	 * 📌 Lista todas las áreas
	 */
	@Transactional
	public List<AreaDto.Response> findByAll(Estado estado) {
		List<AreaDto.Response> areas = null;
		switch (estado) {
		case ACTIVO:
			areas = this.areaDao.findByEstado(true).stream().map(area -> this.modelToResponse(area))
					.collect(Collectors.toList());
			break;
		case INACTIVO:
			areas = this.areaDao.findByEstado(false).stream().map(area -> this.modelToResponse(area))
					.collect(Collectors.toList());
			break;
		case TODOS:
			areas = this.areaDao.findAll().stream().map(area -> this.modelToResponse(area))
					.collect(Collectors.toList());
			break;
		}
		return areas;
	}

	/**
	 * 📌 Búsqueda por nombre
	 */
	@Transactional
	public AreaDto.Response findByNombre(String nombre) {

		/*
		 * AreaModel area =
		 * this.areaDao.findByNombre(this.personalizer.normalizer(nombre))
		 * .orElseThrow(() -> new RuntimeException("Área no encontrada"));
		 * 
		 * return this.modelToResponse(area);
		 */
		return null;
	}

	/**
	 * 📌 Paginación (frontend)
	 */
	@Transactional
	public Page<AreaDto.Response> getAllPaginated(Pageable pageable) {
		return this.areaDao.findAll(pageable).map(area -> this.modelToResponse(area));
	}

	// =========================
	// 🟡 UPDATE
	// =========================

	/**
	 * 📌 Actualización parcial (RECOMENDADO)
	 */
	@Transactional
	public AreaDto.Response updateNombre(AreaDto.Response response) {

		AreaModel area = this.areaDao.findById(response.getId())
				.orElseThrow(() -> new RuntimeException("Área no encontrada"));

		String nombreNormalizado = this.personalizer.normalizer(response.getNombre());

		if (existsByNombre(nombreNormalizado)) {
			throw new RuntimeException("Ya existe un área con ese nombre");
		}

		// area.setNombre(nombreNormalizado);

		this.areaDao.save(area);

		return this.modelToResponse(area);
	}

	/**
	 * 📌 Actualización completa (usar con cuidado)
	 */
	@Transactional
	public AreaDto.Response update(AreaDto.Response response) {

		AreaModel area = this.areaDao.findById(response.getId())
				.orElseThrow(() -> new RuntimeException("Área no encontrada"));

		String nombre = this.personalizer.normalizer(response.getNombre());

		/*
		 * if (!area.getNombre().equals(nombre) && existsByNombre(nombre)) { throw new
		 * RuntimeException("Ya existe un área con ese nombre"); }
		 */

		// area.setNombre(nombre);

		this.areaDao.save(area);

		return this.modelToResponse(area);
	}

	// =========================
	// 🔴 DELETE (SOFT DELETE)
	// =========================

	/**
	 * 📌 Eliminación lógica
	 */
	@Transactional
	public void delete(Long id) {

		AreaModel area = this.areaDao.findById(id).orElseThrow(() -> new RuntimeException("Área no encontrada"));

		area.setEstado(false); // 🔥 soft delete

		this.areaDao.save(area);
	}

	// =========================
	// 🧠 MÉTODOS AUXILIARES
	// =========================

	/**
	 * 📌 Verifica existencia por nombre
	 */
	public boolean existsByNombre(String nombre) {
		return false; // this.areaDao.findByNombre(nombre).isPresent();
	}

	private AreaDto.Response modelToResponse(AreaModel area) {
		return (this.modelMapper.map(area, AreaDto.Response.class));
	}

	private AreaDto.Costo modelToCosto(AreaModel costo) {
		return (this.modelMapper.map(costo, AreaDto.Costo.class));
	}

}