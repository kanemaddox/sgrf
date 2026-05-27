package com.saims.sgrf.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.saims.sgrf.dto.CargoDto;
import com.saims.sgrf.dto.DatosDto;
import com.saims.sgrf.model.CargoModel;

/**
 * Interface CargoService
 * 
 * Define las operaciones de negocio relacionadas con la gestión de cargos.
 * Proporciona métodos para crear, consultar, actualizar y eliminar registros
 * de cargos, así como validaciones de existencia.
 * 
 * Esta interfaz sigue el patrón de separación por capas (Service Layer),
 * actuando como intermediario entre el controlador y el acceso a datos (DAO).
 */
public interface CargoService {

    /**
     * Crea un nuevo Cargo o retorna uno existente en base a los datos proporcionados.
     * 
     * Este método es útil para evitar duplicidad de registros cuando se trabaja
     * con datos provenientes de otras entidades o procesos.
     * 
     * @param datosDtoRequest Objeto que contiene los datos necesarios para crear o buscar el cargo.
     * @return CargoModel existente o recién creado.
     */
    CargoModel createOrGet(DatosDto.Request request);

    /**
     * Crea un nuevo Cargo a partir de un DTO de entrada.
     * 
     * @param cargoDtoRequest DTO con los datos del cargo a registrar.
     * @return CargoDtoResponse DTO con la información del cargo creado.
     */
    CargoDto.Response createCargo(CargoDto.Request request);

    /**
     * Obtiene todos los cargos activos.
     * 
     * Generalmente se basa en un campo de estado (ej: activo = true).
     * 
     * @return Lista de cargos activos.
     */
    List<CargoDto.Response> getAllActive();

    /**
     * Obtiene todos los cargos sin filtro de estado.
     * 
     * @return Lista completa de cargos.
     */
    List<CargoDto.Response> getAll();

    /**
     * Obtiene una lista paginada de cargos.
     * 
     * Permite manejar grandes volúmenes de datos de forma eficiente.
     * 
     * @param pageable Objeto que contiene la configuración de paginación.
     * @return Página de cargos.
     */
    Page<CargoDto.Response> getAllPaginated(Pageable pageable);

    /**
     * Busca un cargo por su ID.
     * 
     * @param id Identificador único del cargo.
     * @return DTO del cargo encontrado.
     */
    CargoDto.Response getById(Long id);

    /**
     * Busca un cargo por su nombre.
     * 
     * @param nombre Nombre del cargo.
     * @return DTO del cargo encontrado.
     */
    CargoDto.Response findByNombre(String nombre);

    /**
     * Actualiza el nombre de un cargo existente.
     * 
     * @param id Identificador del cargo a actualizar.
     * @param nuevoNombre Nuevo nombre del cargo.
     * @return DTO del cargo actualizado.
     */
    CargoDto.Response update(CargoDto.Response response);

    /**
     * Elimina un cargo por su ID.
     * 
     * Dependiendo de la implementación, puede ser una eliminación lógica (soft delete)
     * o física (hard delete).
     * 
     * @param id Identificador del cargo a eliminar.
     */
    void delete(Long id);

    /**
     * Verifica si existe un cargo con el nombre especificado.
     * 
     * @param nombre Nombre del cargo.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByNombre(String nombre);
}
