package com.saims.sgrf.service;

import java.util.List;

import com.saims.sgrf.dto.SucursalAreaCentroCostosDtoResponse;
import com.saims.sgrf.model.AreaModel;
import com.saims.sgrf.model.CentroCostosModel;
import com.saims.sgrf.model.SucursalAreaCentroCostosModel;
import com.saims.sgrf.model.SucursalModel;

/**
 * 📌 Interface SucursalAreaCentroCostos
 * 
 * Define las operaciones de negocio para la gestión de la relación entre:
 * 🏢 Sucursal
 * 🧩 Área
 * 💰 Centro de Costos
 * 
 * Esta entidad representa una asociación entre estas tres dimensiones,
 * comúnmente utilizada para estructurar costos organizacionales.
 */
public interface SucursalAreaCentroCostosService {

    // =========================
    // 🟢 CREATE / GET OR CREATE
    // =========================

    /**
     * 📌 Obtiene un registro existente o lo crea si no existe.
     * 
     * Evita duplicidad en la relación Sucursal-Área-CentroCostos.
     *
     * @param sucursalModel      Modelo de la sucursal
     * @param areaModel          Modelo del área
     * @param centroCostosModel  Modelo del centro de costos
     * @return SucursalAreaCentroCostosModel existente o recién creado
     */
    SucursalAreaCentroCostosModel createOrGet(
        SucursalModel sucursalModel,
        AreaModel areaModel,
        CentroCostosModel centroCostosModel
    );

    // =========================
    // 🔍 READ
    // =========================

    /**
     * 📌 Obtiene un registro por su ID.
     *
     * @param id Identificador único
     * @return Modelo correspondiente
     */
    SucursalAreaCentroCostosModel getById(Long id);

    /**
     * 📌 Lista todos los registros.
     *
     * @return Lista de todas las relaciones
     */
    List<SucursalAreaCentroCostosDtoResponse> getAll();

    /**
     * 📌 Obtiene registros filtrados por sucursal.
     *
     * @param sucursalId ID de la sucursal
     * @return Lista de relaciones asociadas
     */
    List<SucursalAreaCentroCostosModel> getBySucursal(Long sucursalId);
    
    List<SucursalAreaCentroCostosModel>findAll ();

    /**
     * 📌 Obtiene registros filtrados por área.
     *
     * @param areaId ID del área
     * @return Lista de relaciones asociadas
     */
    List<SucursalAreaCentroCostosModel> getByArea(Long areaId);

    /**
     * 📌 Obtiene registros filtrados por centro de costos.
     *
     * @param centroCostosId ID del centro de costos
     * @return Lista de relaciones asociadas
     */
    List<SucursalAreaCentroCostosModel> getByCentroCostos(Long centroCostosId);

    /**
     * 📌 Verifica si existe una relación específica.
     *
     * Útil para evitar duplicados antes de crear registros.
     *
     * @param sucursalId      ID de la sucursal
     * @param areaId          ID del área
     * @param centroCostosId  ID del centro de costos
     * @return true si existe, false caso contrario
     */
    boolean exists(Long sucursalId, Long areaId, Long centroCostosId);

    // =========================
    // 🟡 UPDATE
    // =========================

    /**
     * 📌 Actualiza una relación existente.
     *
     * @param id                 ID del registro a actualizar
     * @param sucursalModel      Nuevo modelo de sucursal
     * @param areaModel          Nuevo modelo de área
     * @param centroCostosModel  Nuevo modelo de centro de costos
     * @return Modelo actualizado
     */
    SucursalAreaCentroCostosModel update(
        Long id,
        SucursalModel sucursalModel,
        AreaModel areaModel,
        CentroCostosModel centroCostosModel
    );

    // =========================
    // 🔴 DELETE
    // =========================

    /**
     * 📌 Elimina un registro por su ID.
     *
     * @param id Identificador del registro
     */
    void delete(Long id);
}
