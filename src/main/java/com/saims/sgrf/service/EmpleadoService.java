package com.saims.sgrf.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.saims.sgrf.dto.DatosDtoRequest;
import com.saims.sgrf.dto.EmpleadoDtoRequest;
import com.saims.sgrf.dto.EmpleadoDtoResponse;
import com.saims.sgrf.model.CargoModel;
import com.saims.sgrf.model.EmpleadoModel;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.model.SucursalModel;

/**
 * Servicio encargado de la gestión de empleados.
 * 
 * Define las operaciones principales para el manejo de empleados dentro del sistema,
 * incluyendo creación, consulta, actualización de estado y eliminación.
 * 
 * Esta interfaz debe ser implementada por una clase de servicio (por ejemplo: EmpleadoServiceImpl),
 * donde se definirá la lógica de negocio correspondiente.
 */
public interface EmpleadoService {

    /**
     * Registra o actualiza los datos de un empleado.
     * 
     * Este método centraliza la creación de un empleado, asociándolo con:
     * - Persona
     * - Cargo
     * - Profesión
     * - Sucursal
     * 
     * @param personaModel   Datos de la persona asociada al empleado
     * @param cargoModel     Cargo que desempeña el empleado
     * @param profesionModel Profesión del empleado
     * @param sucursalModel  Sucursal donde trabaja
     * @param datosDtoRequest Datos adicionales necesarios para el registro
     * @return EmpleadoModel entidad del empleado creada o actualizada
     */
    EmpleadoModel addDatos(
        PersonaModel personaModel,
        CargoModel cargoModel,
        SucursalModel sucursalModel,
        DatosDtoRequest datosDtoRequest
    );
    
    EmpleadoDtoResponse create(EmpleadoDtoRequest request);

    /**
     * Obtiene un empleado por su identificador único.
     * 
     * @param id Identificador del empleado
     * @return DTO con la información del empleado
     */
    EmpleadoDtoResponse getById(Long id);

    /**
     * Obtiene la lista completa de empleados.
     * 
     * @return Lista de empleados en formato DTO
     */
    List<EmpleadoDtoResponse> getAll();
    
    List<EmpleadoModel> findAll();

    /**
     * Obtiene una lista paginada de empleados.
     * 
     * Permite manejar grandes volúmenes de datos de forma eficiente.
     * 
     * @param pageable Configuración de paginación (página, tamaño, orden)
     * @return Página de empleados en formato DTO
     */
    Page<EmpleadoDtoResponse> getAllPaginated(Pageable pageable);

    /**
     * Busca un empleado a partir del ID de la persona asociada.
     * 
     * Útil cuando la relación persona-empleado es uno a uno.
     * 
     * @param personaId ID de la persona
     * @return DTO del empleado encontrado
     */
    EmpleadoDtoResponse findByPersonaId(Long personaId);

    /**
     * Cambia el estado de un empleado (activo/inactivo).
     * 
     * Este método se utiliza comúnmente para realizar eliminaciones lógicas
     * o habilitar/deshabilitar empleados sin perder la información.
     * 
     * @param id     ID del empleado
     * @param estado Nuevo estado (true = activo, false = inactivo)
     */
    void cambiarEstado(Long id, boolean estado);

    /**
     * Elimina un empleado por su ID.
     * 
     * Dependiendo de la implementación, puede ser:
     * - Eliminación física (delete real en BD)
     * - Eliminación lógica (cambio de estado)
     * 
     * @param id ID del empleado a eliminar
     */
    void delete(Long id);
    
    void setAllEstado(boolean estado);
}
