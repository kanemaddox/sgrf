package com.saims.sgrf.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.CargoDao;
import com.saims.sgrf.dto.CargoDtoRequest;
import com.saims.sgrf.dto.CargoDtoResponse;
import com.saims.sgrf.dto.DatosDtoRequest;
import com.saims.sgrf.model.CargoModel;
import com.saims.sgrf.service.CargoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * 📌 Servicio de implementación para la gestión de Cargos
 * 
 * Contiene la lógica de negocio para:
 * ✔ Crear cargos
 * ✔ Obtener cargos
 * ✔ Buscar por nombre
 * ✔ Paginación
 * ✔ Actualización y eliminación
 */
@Service
@RequiredArgsConstructor
public class CargoServiceImpl implements CargoService {

    private final CargoDao cargoDao;
    private final ModelMapper modelMapper;
    private Personalizer personalizer = new Personalizer();

    /**
     * 📌 Crea o recupera un Cargo basado en DatosDtoRequest
     * 
     * 🔎 Flujo:
     * 1. Normaliza el nombre (uppercase + trim)
     * 2. Busca si ya existe
     * 3. Si no existe → lo crea
     */
    @Transactional
    public CargoModel createOrGet(DatosDtoRequest datosDtoRequest) {

        String nombreCargo = this.personalizer.normalizer(datosDtoRequest.getCargoNombre());

        return this.cargoDao.findByNombre(nombreCargo)
                .orElseGet(() -> {
                    CargoModel nuevo = new CargoModel();
                    nuevo.setNombre(nombreCargo);
                    return this.cargoDao.save(nuevo);
                });
    }

    /**
     * 📌 Crea un nuevo Cargo o devuelve uno existente
     */
    @Transactional
    public CargoDtoResponse createCargo(CargoDtoRequest cargoDtoRequest) {

        String nombreCargo = this.personalizer.normalizer(cargoDtoRequest.getNombre());
        
        if (existsByNombre(nombreCargo)) {
            throw new RuntimeException("El área ya existe");
        }

        CargoModel cargo = new CargoModel();
        cargo.setNombre(nombreCargo);
        cargo.setEstado(true);

        this.cargoDao.save(cargo);

        return modelToResponse(cargo);
    }
    
    /**
     * 📌 Lista todas las cargos activas
     */
    @Transactional
    public List<CargoDtoResponse> getAllActive() {

        return this.cargoDao.findAll().stream()
                .filter(CargoModel::getEstado) // 🔥 solo activos
                .map(cargo->this.modelToResponse(cargo))
                .collect(Collectors.toList());
    }

    /**
     * 📌 Obtiene todos los cargos
     */
    @Transactional
    public List<CargoDtoResponse> getAll() {

        return this.cargoDao.findAll().stream()
                .map(cargo->this.modelToResponse(cargo))
                .collect(Collectors.toList());
    }

    /**
     * 📌 Obtiene todos los cargos con paginación
     */
    @Transactional
    public Page<CargoDtoResponse> getAllPaginated(Pageable pageable) {

        return this.cargoDao.findAll(pageable)
                .map(cargo->this.modelToResponse(cargo));
    }

    /**
     * 📌 Busca un cargo por ID
     */
    @Transactional
    public CargoDtoResponse getById(Long id) {

        CargoModel cargo = this.cargoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Cargo no encontrado con ID: " + id));

        return this.modelToResponse(cargo);
    }

    /**
     * 📌 Busca un cargo por nombre
     */
    @Transactional
    public CargoDtoResponse findByNombre(String nombre) {

        String nombreCargo = this.personalizer.normalizer(nombre);

        CargoModel cargo = this.cargoDao.findByNombre(nombreCargo)
                .orElseThrow(() -> new RuntimeException("Cargo no encontrado: " + nombre));

        return this.modelToResponse(cargo);
    }

    /**
     * 📌 Actualiza el nombre de un cargo
     */
    @Transactional
    public CargoDtoResponse update(CargoDtoResponse response) {

        CargoModel cargo = new CargoModel();
        cargo.setId(response.getId());
        cargo.setNombre(this.personalizer.normalizer(response.getNombre()));
        cargo.setEstado(response.getEstado());
        this.cargoDao.save(cargo);

        return this.modelToResponse(cargo);
    }

    /**
     * 📌 Elimina un cargo por ID
     */
    @Transactional
    public void delete(Long id) {

        if (!this.cargoDao.existsById(id)) {
            throw new RuntimeException("Cargo no existe con ID: " + id);
        }

        this.cargoDao.deleteById(id);
    }

    // =========================
    // 🔧 MÉTODOS PRIVADOS
    // =========================

    /**
     * 📌 Verifica existencia por nombre
     */
    public boolean existsByNombre(String nombre) {
        return this.cargoDao.findByNombre(nombre).isPresent();
    }

    /**
     * 📌 Convierte Entity → DTO
     */
    private CargoDtoResponse modelToResponse(CargoModel cargoModel) {
        return this.modelMapper.map(cargoModel, CargoDtoResponse.class);
    }
}