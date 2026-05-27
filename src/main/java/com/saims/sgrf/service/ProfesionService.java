package com.saims.sgrf.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.saims.sgrf.dto.DatosDto;
import com.saims.sgrf.dto.ProfesionDto;
import com.saims.sgrf.model.ProfesionModel;

/**
 * 🧩 Interfaz de servicio para la gestión de Profesiones.
 * 
 * Define las operaciones básicas (CRUD) y consultas adicionales
 * relacionadas con la entidad Profesion.
 * 
 * Se utiliza como contrato para la implementación de la lógica de negocio.
 */
public interface ProfesionService {

    /**
     * 📌 Crea una profesión o la obtiene si ya existe.
     * 
     * Este método es útil para evitar duplicados en base al nombre u otros datos.
     * 
     * @param datosDtoRequest Datos necesarios para la creación o búsqueda.
     * @return ProfesionModel existente o recién creada.
     */
    ProfesionModel createOrGet(DatosDto.Request request);

    /**
     * 📌 Crea una nueva profesión.
     * 
     * @param request DTO con los datos de la profesión.
     * @return ProfesionDtoResponse con la información creada.
     */
    ProfesionDto.Response createProfesion(ProfesionDto.Request request);

    /**
     * 📌 Obtiene todas las profesiones registradas.
     * 
     * @return Lista de ProfesionDtoResponse.
     */
    List<ProfesionDto.Response> getAll();

    /**
     * 📌 Obtiene una profesión por su ID.
     * 
     * @param id Identificador único de la profesión.
     * @return ProfesionDtoResponse correspondiente.
     */
    ProfesionDto.Response getById(Long id);

    /**
     * 📌 Busca una profesión por su nombre.
     * 
     * @param nombre Nombre de la profesión.
     * @return ProfesionDtoResponse encontrada.
     */
    ProfesionDto.Response findByNombre(String nombre);

    /**
     * 📌 Verifica si existe una profesión por su nombre.
     * 
     * @param nombre Nombre de la profesión.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByNombre(String nombre);

    /**
     * 📌 Obtiene todas las profesiones de forma paginada.
     * 
     * Ideal para manejar grandes volúmenes de datos.
     * 
     * @param pageable Configuración de paginación.
     * @return Página de ProfesionDtoResponse.
     */
    Page<ProfesionDto.Response> getAllPaginated(Pageable pageable);

    /**
     * 📌 Actualiza los datos de una profesión existente.
     * 
     * @param response Datos actualizados de la profesión.
     * @return ProfesionDtoResponse con los cambios aplicados.
     */
    ProfesionDto.Response update(ProfesionDto.Response response);

    /**
     * 📌 Elimina una profesión por su ID.
     * 
     * @param id Identificador de la profesión a eliminar.
     */
    void delete(Long id);
}
