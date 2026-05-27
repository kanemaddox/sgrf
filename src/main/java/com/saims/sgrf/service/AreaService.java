package com.saims.sgrf.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.saims.sgrf.dto.AreaDto;
import com.saims.sgrf.dto.DatosDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.AreaModel;

/**
 * 📌 Interfaz: AreaService
 * 
 * 🎯 Define el contrato de operaciones para la gestión de áreas.
 * 
 * 📂 Pertenece a la capa de servicio (Service Layer), donde se implementa la lógica de negocio.
 */
public interface AreaService {

    /**
     * ✅ Crea una nueva área
     * @param request Datos de entrada del área
     * @return Área creada en formato DTO
     */
    AreaDto.Response createArea(AreaDto.Request request);

    /**
     * 🔁 Crea un área si no existe, o devuelve una existente
     * @param request Datos de entrada
     * @return Entidad AreaModel
     * utilizado para cargas masivas de datos 
     */
    AreaModel createOrGet(DatosDto.Request request);

    /**
     * 🔍 Obtiene un área por su ID
     * @param id Identificador del área
     * @return Área encontrada
     */
    AreaDto.Response getById(Long id);

    /**
     * 📋 Lista todas las áreas
     * @return Lista completa de áreas
     */
    List<AreaDto.Response> findByAll(Estado estado);

    /**
     * 🔎 Busca un área por nombre
     * @param nombre Nombre del área
     * @return Área encontrada
     */
    AreaDto.Response findByNombre(String nombre);

    /**
     * 📄 Obtiene áreas de forma paginada
     * @param pageable Configuración de paginación
     * @return Página de áreas
     */
    Page<AreaDto.Response> getAllPaginated(Pageable pageable);

    /**
     * ✏️ Actualiza solo el nombre del área
     * @param id ID del área
     * @param nuevoNombre Nuevo nombre
     * @return Área actualizada
     */
    AreaDto.Response updateNombre(AreaDto.Response response);

    /**
     * 🔄 Actualiza completamente un área
     * @param id ID del área
     * @param request Nuevos datos
     * @return Área actualizada
     */
    AreaDto.Response update(AreaDto.Response response);

    /**
     * ❌ Elimina un área por ID
     * @param id ID del área
     */
    void delete(Long id);

    /**
     * ✔️ Verifica si existe un área con ese nombre
     * @param nombre Nombre a validar
     * @return true si existe, false si no
     */
    boolean existsByNombre(String nombre);
}
