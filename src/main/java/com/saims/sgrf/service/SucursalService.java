package com.saims.sgrf.service;

import java.util.List;

import com.saims.sgrf.dto.DatosDtoRequest;
import com.saims.sgrf.dto.SucursalDtoResponse;
import com.saims.sgrf.model.SucursalModel;

/**
 * 📌 Servicio para la gestión de Sucursales
 * 
 * Define las operaciones principales para la administración de sucursales,
 * incluyendo creación, consulta, actualización y eliminación.
 */
public interface SucursalService {

    // =========================
    // 🟢 CREATE / GET OR CREATE
    // =========================

    /**
     * 📌 Crea una sucursal si no existe, o la retorna si ya está registrada.
     * 
     * @param datosDtoRequest Objeto con los datos necesarios para la creación
     * @return SucursalModel entidad creada o existente
     */
    SucursalModel createOrGet(DatosDtoRequest datosDtoRequest);

    /**
     * 📌 Crea una nueva sucursal
     * 
     * @param response DTO con los datos de la sucursal
     * @return SucursalDtoResponse sucursal creada
     */
    SucursalDtoResponse create(SucursalDtoResponse response);


    // =========================
    // 🔵 READ
    // =========================

    /**
     * 📌 Obtiene una sucursal por su ID
     * 
     * @param id Identificador único de la sucursal
     * @return SucursalDtoResponse sucursal encontrada
     */
    SucursalDtoResponse getSucursal(Long id);

    /**
     * 📌 Obtiene todas las sucursales
     * 
     * @return Lista de sucursales
     */
    List<SucursalDtoResponse> getAll();

    /**
     * 📌 Busca una sucursal por su código
     * 
     * @param codigo Código único de la sucursal
     * @return SucursalDtoResponse sucursal encontrada
     */
    SucursalDtoResponse getByCodigo(String codigo);

    /**
     * 📌 Busca una sucursal por su nombre
     * 
     * @param nombre Nombre de la sucursal
     * @return SucursalDtoResponse sucursal encontrada
     */
    SucursalDtoResponse findByNombre(String nombre);


    // =========================
    // 🟡 VALIDACIONES
    // =========================

    /**
     * 📌 Verifica si existe una sucursal con el código dado
     * 
     * @param codigo Código de la sucursal
     * @return true si existe, false en caso contrario
     */
    boolean existsByCodigo(String codigo);


    // =========================
    // 🟠 UPDATE
    // =========================

    /**
     * 📌 Actualiza los datos de una sucursal existente
     * 
     * @param response DTO con los nuevos datos
     * @return SucursalDtoResponse sucursal actualizada
     */
    SucursalDtoResponse update(SucursalDtoResponse response);


    // =========================
    // 🔴 DELETE
    // =========================

    /**
     * 📌 Elimina una sucursal por su ID
     * 
     * @param id Identificador de la sucursal a eliminar
     */
    void delete(Long id);
}
