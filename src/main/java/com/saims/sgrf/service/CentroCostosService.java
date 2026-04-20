package com.saims.sgrf.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.saims.sgrf.dto.CentroCostosDtoRequest;
import com.saims.sgrf.dto.CentroCostosDtoResponse;
import com.saims.sgrf.dto.DatosDtoRequest;
import com.saims.sgrf.model.CentroCostosModel;

/**
 * Servicio encargado de la gestión de Centros de Costos.
 * 
 * <p>Define las operaciones principales para la creación, consulta,
 * actualización y eliminación de centros de costos dentro del sistema.</p>
 * 
 * <p>Incluye métodos tanto para operaciones directas como para 
 * manejo de datos provenientes de DTOs.</p>
 */
public interface CentroCostosService {

    /**
     * Crea un nuevo Centro de Costos o retorna uno existente
     * basado en los datos proporcionados.
     *
     * @param datosDtoRequest objeto con datos generales (nombre, código, etc.)
     * @return entidad {@link CentroCostosModel} creada o encontrada
     */
    CentroCostosModel createOrGet(DatosDtoRequest datosDtoRequest);

    /**
     * Crea un nuevo Centro de Costos.
     *
     * @param request DTO con la información necesaria para la creación
     * @return DTO de respuesta con los datos del centro creado
     */
    CentroCostosDtoResponse createCentroCostos(CentroCostosDtoRequest request);

    /**
     * Obtiene un Centro de Costos por su ID.
     *
     * @param id identificador único
     * @return DTO con la información del centro de costos
     */
    CentroCostosDtoResponse getById(Long id);

    /**
     * Obtiene la lista completa de Centros de Costos.
     *
     * @return lista de DTOs de centros de costos
     */
    List<CentroCostosDtoResponse> getAll();

    /**
     * Obtiene una lista paginada de Centros de Costos.
     *
     * @param pageable configuración de paginación
     * @return página de DTOs de centros de costos
     */
    Page<CentroCostosDtoResponse> getAllPaginated(Pageable pageable);

    /**
     * Busca un Centro de Costos por su código.
     *
     * @param codigo código único del centro de costos
     * @return DTO con la información encontrada
     */
    CentroCostosDtoResponse findByCodigo(String codigo);

    /**
     * Actualiza únicamente el nombre de un Centro de Costos.
     *
     * @param response DTO con los datos actualizados
     * @return DTO con la información actualizada
     */
    CentroCostosDtoResponse updateNombre(CentroCostosDtoResponse response);

    /**
     * Actualiza completamente un Centro de Costos.
     *
     * @param response DTO con todos los campos actualizados
     * @return DTO con la información actualizada
     */
    CentroCostosDtoResponse updateFull(CentroCostosDtoResponse response);

    /**
     * Elimina un Centro de Costos por su ID.
     *
     * @param id identificador único del centro de costos
     */
    void delete(Long id);
}
