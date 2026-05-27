package com.saims.sgrf.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.saims.sgrf.dto.BancoDto;
import com.saims.sgrf.dto.DatosDto;
import com.saims.sgrf.model.BancoModel;

/**
 * 📌 Interface: BancoService
 * 
 * 🧾 Descripción:
 * Define los métodos de negocio para la gestión de bancos.
 * Esta interfaz debe ser implementada por la clase BancoServiceImpl.
 * 
 * 🎯 Responsabilidad:
 * - Manejar la lógica de negocio relacionada a Banco
 * - Servir de intermediario entre Controller y Repository
 */
public interface BancoService {

    /**
     * 🆕 Crea un nuevo banco a partir de un DTO de solicitud.
     *
     * @param request Datos necesarios para crear el banco
     * @return BancoDtoResponse con la información del banco creado
     */
    BancoDto.Response createBanco(BancoDto.Request request);

    /**
     * 🔍 Busca un banco por nombre o lo crea si no existe.
     *
     * @param nombre Nombre del banco
     * @return Entidad BancoModel existente o recién creada
     */
    BancoModel createOrGet(DatosDto.Request request);

    /**
     * 🔎 Obtiene un banco por su ID.
     *
     * @param id Identificador del banco
     * @return BancoDtoResponse con los datos encontrados
     */
    BancoDto.Response getById(Long id);

    /**
     * 📋 Lista todos los bancos.
     *
     * @return Lista de BancoDtoResponse
     */
    List<BancoDto.Response> getAll();

    /**
     * 📄 Obtiene todos los bancos de forma paginada.
     *
     * @param pageable Configuración de paginación (número de página, tamaño, orden)
     * @return Página de BancoDtoResponse
     */
    Page<BancoDto.Response> getAllPaginated(Pageable pageable);

    /**
     * 🔍 Busca un banco por su nombre.
     *
     * @param nombre Nombre del banco
     * @return BancoDtoResponse encontrado
     */
    BancoDto.Response findByNombre(String nombre);

    /**
     * ❌ Elimina un banco por su ID.
     *
     * @param id Identificador del banco
     */
    void delete(Long id);

    /**
     * ✏️ Actualiza únicamente el nombre de un banco.
     *
     * @param id ID del banco a actualizar
     * @param nuevoNombre Nuevo nombre del banco
     * @return BancoDtoResponse actualizado
     */
    BancoDto.Response updateNombre(BancoDto.Response response);

    /**
     * 🔄 Actualiza completamente los datos de un banco.
     *
     * @param id ID del banco a actualizar
     * @param request DTO con los nuevos datos
     * @return BancoDtoResponse actualizado
     */
    BancoDto.Response update(BancoDto.Response response);
}
