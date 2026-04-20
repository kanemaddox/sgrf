package com.saims.sgrf.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.ProfesionDao;
import com.saims.sgrf.dto.DatosDtoRequest;
import com.saims.sgrf.dto.ProfesionDtoRequest;
import com.saims.sgrf.dto.ProfesionDtoResponse;
import com.saims.sgrf.model.ProfesionModel;
import com.saims.sgrf.service.ProfesionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfesionServiceImpl implements ProfesionService {

    private final ProfesionDao profesionDao;
    private final ModelMapper modelMapper;
    private Personalizer personalizer = new Personalizer();

    // =========================
    // 🟢 CREATE OR GET
    // =========================

    /**
     * 📌 Crea o recupera una profesión a partir de DatosDtoRequest
     * 
     * 🔎 Flujo:
     * 1. Normaliza el nombre de la profesión (uppercase + trim)
     * 2. Verifica si ya existe en base de datos
     * 3. Si existe → retorna la existente
     * 4. Si no existe → crea una nueva profesión
     * 
     * @param datosDtoRequest datos de entrada
     * @return ProfesionModel existente o creada
     */
    @Transactional
    public ProfesionModel createOrGet(DatosDtoRequest datosDtoRequest) {

        String nombreProfesion = this.personalizer.normalizer(datosDtoRequest.getProfesionNombre());

        return profesionDao.findByNombre(nombreProfesion)
                .orElseGet(() -> {
                    ProfesionModel nuevo = new ProfesionModel();
                    nuevo.setNombre(nombreProfesion);
                    return profesionDao.save(nuevo);
                });
    }

    /**
     * 📌 Crea o recupera una profesión a partir de ProfesionDtoRequest
     * 
     * @param request datos de la profesión
     * @return ProfesionDtoResponse
     */
    @Transactional
    public ProfesionDtoResponse createProfesion(ProfesionDtoRequest request) {

        String nombreProfesion = this.personalizer.normalizer(request.getNombre());

        ProfesionModel profesionModel = this.profesionDao.findByNombre(nombreProfesion)
                .orElseGet(() -> {
                    ProfesionModel nuevaProfesion = new ProfesionModel();
                    nuevaProfesion.setNombre(nombreProfesion);
                    return profesionDao.save(nuevaProfesion);
                });

        return modelMapper.map(profesionModel, ProfesionDtoResponse.class);
    }

    // =========================
    // 🔵 READ
    // =========================

    /**
     * 📌 Obtiene todas las profesiones registradas
     * 
     * @return lista de ProfesionDtoResponse
     */
    @Transactional
    public List<ProfesionDtoResponse> getAll() {

        return profesionDao.findAll()
                .stream()
                .map(profesion -> this.modelToResponse(profesion))
                .collect(Collectors.toList());
    }

    /**
     * 📌 Busca una profesión por ID
     * 
     * @param id identificador de la profesión
     * @return ProfesionDtoResponse
     */
    @Transactional
    public ProfesionDtoResponse getById(Long id) {

        ProfesionModel profesion = this.profesionDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesión no encontrada con ID: " + id));

        return this.modelToResponse(profesion);
    }

    /**
     * 📌 Busca una profesión por nombre
     * 
     * @param nombre nombre de la profesión
     * @return ProfesionDtoResponse
     */
    @Transactional
    public ProfesionDtoResponse findByNombre(String nombre) {

        String nombreNormalizado = this.personalizer.normalizer(nombre);

        ProfesionModel profesion = profesionDao.findByNombre(nombreNormalizado)
                .orElseThrow(() -> new RuntimeException("Profesión no encontrada: " + nombre));

        return this.modelToResponse(profesion);
    }
    
    /**
     * 📌 Paginación (frontend)
     */
    @Transactional
    public Page<ProfesionDtoResponse> getAllPaginated(Pageable pageable) {
        return this.profesionDao.findAll(pageable)
                .map(area -> this.modelToResponse(area));
    }

    // =========================
    // 🟡 UPDATE
    // =========================

    /**
     * 📌 Actualiza una profesión existente
     * 
     * @param request datos actualizados
     * @return ProfesionDtoResponse
     */
    @Transactional
    public ProfesionDtoResponse update(ProfesionDtoResponse response) {

        ProfesionModel profesion = this.profesionDao.findById(response.getId())
                .orElseThrow(() -> new RuntimeException("Profesión no encontrada con ID: " + response.getId()));

        String nombreNormalizado = this.personalizer.normalizer(response.getNombre());

        profesion.setNombre(nombreNormalizado);

        return this.modelToResponse(profesion);
    }

    // =========================
    // 🔴 DELETE
    // =========================

    /**
     * 📌 Elimina una profesión por ID
     * 
     * @param id identificador de la profesión
     */
    @Transactional
    public void delete(Long id) {

        if (!profesionDao.existsById(id)) {
            throw new RuntimeException("Profesión no encontrada con ID: " + id);
        }

        this.profesionDao.deleteById(id);
    }

    // =========================
    // ⚙️ MÉTODOS PRIVADOS
    // =========================

    /**
     * 📌 Normaliza el nombre de la profesión
     * 
     * @param nombre nombre original
     * @return nombre normalizado
     */
    
    private ProfesionDtoResponse modelToResponse(ProfesionModel profesion) {
    	return (this.modelMapper.map(profesion, ProfesionDtoResponse.class));
    }
    
    /**
     * 📌 Verifica existencia por nombre
     */
    public boolean existsByNombre(String nombre) {
        return this.profesionDao.findByNombre(nombre).isPresent();
    }
}