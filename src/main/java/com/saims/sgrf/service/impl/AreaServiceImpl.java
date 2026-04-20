package com.saims.sgrf.service.impl;

import java.util.List;
import java.util.stream.Collectors;

//import org.hibernate.internal.util.collections.AbstractPagedArray.Page;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.AreaDao;
import com.saims.sgrf.dto.AreaDtoRequest;
import com.saims.sgrf.dto.AreaDtoResponse;
import com.saims.sgrf.dto.DatosDtoRequest;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.AreaModel;
import com.saims.sgrf.service.AreaService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService {

    private final AreaDao areaDao;
    private final ModelMapper modelMapper;
    private Personalizer personalizer = new Personalizer();

    // =========================
    // 🟢 CREATE
    // =========================

    /**
     * 📌 Crea un área evitando duplicados
     */
    @Transactional
    public AreaDtoResponse createArea(AreaDtoRequest request) {

        String nombre = this.personalizer.normalizer(request.getNombre());

        if (existsByNombre(nombre)) {
            throw new RuntimeException("El área ya existe");
        }

        AreaModel area = new AreaModel();
        area.setNombre(nombre);
        area.setEstado(true);

        this.areaDao.save(area);

        return this.modelToResponse(area);
    }

    /**
     * 📌 Uso interno o cargas masivas
     */
    @Transactional
    public AreaModel createOrGet(DatosDtoRequest request) {

        String nombre = this.personalizer.normalizer(request.getNombreArea());

        return this.areaDao.findByNombre(nombre)
                .orElseGet(() -> {
                    AreaModel nueva = new AreaModel();
                    nueva.setNombre(nombre);
                    nueva.setEstado(true);
                    return this.areaDao.save(nueva);
                });
    }

    // =========================
    // 🔵 READ
    // =========================

    /**
     * 📌 Obtiene un área por ID
     */
    @Transactional
    public AreaDtoResponse getById(Long id) {

        AreaModel area = this.areaDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Área no encontrada con ID: " + id));

        return this.modelToResponse(area);
    }
    
    /**
     * 📌 Lista todas las áreas
     */
    @Transactional
    public List<AreaDtoResponse> findByAll(Estado estado) {
    	
    	List<AreaDtoResponse>areas = null;
    	switch(estado) {
    		case ACTIVO:
    			areas = this.areaDao.findByEstado(true).stream()
    			.map(area->this.modelToResponse(area))
    			.collect(Collectors.toList());
    			break;
    		case INACTIVO:
    			areas = this.areaDao.findByEstado(false).stream()
    			.map(area->this.modelToResponse(area))
    			.collect(Collectors.toList());
    			break;
    		case TODOS:
    			areas = this.areaDao.findAll().stream()
                .map(area -> this.modelToResponse(area))
                .collect(Collectors.toList());
    			break;
    	}

        return areas;
    }

    /**
     * 📌 Búsqueda por nombre
     */
    @Transactional
    public AreaDtoResponse findByNombre(String nombre) {

        AreaModel area = this.areaDao.findByNombre(this.personalizer.normalizer(nombre))
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        return this.modelToResponse(area);
    }

    /**
     * 📌 Paginación (frontend)
     */
    @Transactional
    public Page<AreaDtoResponse> getAllPaginated(Pageable pageable) {
        return this.areaDao.findAll(pageable)
                .map(area -> this.modelToResponse(area));
    }

    // =========================
    // 🟡 UPDATE
    // =========================

    /**
     * 📌 Actualización parcial (RECOMENDADO)
     */
    @Transactional
    public AreaDtoResponse updateNombre(AreaDtoResponse response) {

        AreaModel area = this.areaDao.findById(response.getId())
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        String nombreNormalizado = this.personalizer.normalizer(response.getNombre());

        if (existsByNombre(nombreNormalizado)) {
            throw new RuntimeException("Ya existe un área con ese nombre");
        }

        area.setNombre(nombreNormalizado);

        this.areaDao.save(area);

        return this.modelToResponse(area);
    }

    /**
     * 📌 Actualización completa (usar con cuidado)
     */
    @Transactional
    public AreaDtoResponse update(AreaDtoResponse response) {

        AreaModel area = this.areaDao.findById(response.getId())
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        String nombre = this.personalizer.normalizer(response.getNombre());

        if (!area.getNombre().equals(nombre) && existsByNombre(nombre)) {
            throw new RuntimeException("Ya existe un área con ese nombre");
        }

        area.setNombre(nombre);

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

        AreaModel area = this.areaDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        area.setEstado(false); // 🔥 soft delete

        this.areaDao.save(area);
    }

    // =========================
    // 🧠 MÉTODOS AUXILIARES
    // =========================

    /**
     * 📌 Verifica existencia por nombre
     */
    public  boolean existsByNombre(String nombre) {
        return this.areaDao.findByNombre(nombre).isPresent();
    }
    
    private AreaDtoResponse modelToResponse(AreaModel area) {
    	return (this.modelMapper.map(area, AreaDtoResponse.class));
    }
    
}