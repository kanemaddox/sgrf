package com.saims.sgrf.service;

import java.util.List;

import com.saims.sgrf.dto.DatosDtoRequest;
import com.saims.sgrf.dto.PersonaDtoRequest;
import com.saims.sgrf.dto.PersonaDtoResponse;
import com.saims.sgrf.model.PersonaModel;

/**
 * Servicio encargado de gestionar la lógica de negocio relacionada con la entidad Persona.
 * 
 * Define las operaciones principales para la creación, consulta, actualización y eliminación
 * de personas, así como validaciones asociadas (por ejemplo, existencia por IDP).
 * 
 * Este servicio actúa como una capa intermedia entre los controladores y el acceso a datos (DAO/Repository).
 */
public interface PersonaService {

    /**
     * Crea una nueva persona o recupera una existente en base a los datos proporcionados.
     * 
     * Este método es útil para evitar duplicidad de registros, generalmente validando
     * por un identificador único como el IDP.
     *
     * @param datosDtoRequest Objeto con los datos necesarios para identificar o crear la persona.
     * @return PersonaModel entidad existente o recién creada.
     */
    PersonaModel createOrGet(DatosDtoRequest datosDtoRequest);

    /**
     * Crea una nueva persona en el sistema.
     * 
     * A diferencia de createOrGet, este método asume que la persona no existe previamente
     * y realiza el registro directo.
     *
     * @param request DTO con la información de la persona a registrar.
     * @return PersonaModel entidad creada.
     */
    PersonaModel createPersona(PersonaDtoRequest request);

    /**
     * Obtiene una persona por su identificador único.
     *
     * @param id Identificador único de la persona.
     * @return PersonaDtoResponse DTO con la información de la persona.
     */
    PersonaDtoResponse getById(Long id);

    /**
     * Busca una persona utilizando su IDP (Identificador Personal).
     *
     * @param idp Identificador único de la persona.
     * @return PersonaDtoResponse DTO con la información encontrada.
     */
    PersonaDtoResponse findByIdp(String idp);

    /**
     * Obtiene la lista completa de personas registradas.
     *
     * @return Lista de PersonaDtoResponse con todas las personas.
     */
    List<PersonaDtoResponse> getAll();
    
    List<PersonaModel>findAll();

    /**
     * Actualiza la información de una persona existente.
     *
     * @param request DTO con los nuevos datos de la persona.
     * @return PersonaDtoResponse DTO con la información actualizada.
     */
    PersonaDtoResponse update(PersonaDtoRequest request);

    /**
     * Elimina una persona del sistema por su ID.
     *
     * @param id Identificador de la persona a eliminar.
     */
    void delete(Long id);

    /**
     * Verifica si existe una persona con el IDP proporcionado.
     *
     * @param idp Identificador único de la persona.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByIdp(String idp);
}
