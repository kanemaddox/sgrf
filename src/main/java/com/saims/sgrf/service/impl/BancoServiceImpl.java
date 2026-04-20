package com.saims.sgrf.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.BancoDao;
import com.saims.sgrf.dto.BancoDtoRequest;
import com.saims.sgrf.dto.BancoDtoResponse;
import com.saims.sgrf.dto.DatosDtoRequest;
import com.saims.sgrf.model.BancoModel;
import com.saims.sgrf.service.BancoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

/**
 * 📌 Clase: BancoServiceImpl
 * 
 * 🧾 Descripción:
 * Implementación del servicio para la gestión de bancos.
 * Contiene la lógica de negocio para registrar, obtener y listar bancos,
 * evitando duplicados mediante la validación por nombre.
 * 
 * 🔧 Tecnologías:
 * - Spring Boot (@Service, @Transactional)
 * - JPA (BancoDao)
 * - ModelMapper (conversión Entity ↔ DTO)
 */
@Service
@RequiredArgsConstructor
public class BancoServiceImpl implements BancoService {

    private final BancoDao bancoDao;
    private final ModelMapper modelMapper;
    private Personalizer personalizer = new Personalizer();
    
    // =========================
    // 🟢 CREATE
    // =========================    
    /**
     * 📌 Crea un banco evitando duplicados
     */
    @Transactional
    public BancoDtoResponse createBanco(BancoDtoRequest request) {

        String nombre = this.personalizer.normalizer(request.getNombre());

        if (existsByNombre(nombre)) {
            throw new RuntimeException("El área ya existe");
        }

        BancoModel banco = new BancoModel();
        banco.setNombre(nombre);
        banco.setEstado(true);
        
        this.bancoDao.save(banco);

        return this.modelToResponse(banco);
    }

    /**
     * 📌 Método: createOrGet
     * 
     * 🧾 Descripción:
     * Busca un banco por nombre. Si no existe, lo crea.
     * Este método es reutilizable para otras entidades (patrón "find or create").
     * 
     * @param nombre Nombre del banco
     * @return BancoModel existente o creado
     */
    @Transactional
    public BancoModel createOrGet(DatosDtoRequest request) {
        String nombreNormalizado = this.personalizer.normalizer(request.getBancoNombre());

        return this.bancoDao.findByNombre(nombreNormalizado)
                .orElseGet(() -> {
                    BancoModel nuevo = new BancoModel();
                    nuevo.setNombre(nombreNormalizado);
                    nuevo.setEstado(true);
                    return this.bancoDao.save(nuevo);
                });
    }

    /**
     * 📌 Método: getById
     * 
     * 🧾 Descripción:
     * Obtiene un banco por ID.
     * 
     * @param id ID del banco
     * @return BancoDtoResponse
     */
    @Transactional
    public BancoDtoResponse getById(Long id) {
        BancoModel banco = bancoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Banco no encontrado con ID: " + id));

        return this.modelToResponse(banco);
    }

    /**
     * 📌 Método: getAll
     * 
     * 🧾 Descripción:
     * Lista todos los bancos.
     * 
     * @return Lista de BancoDtoResponse
     */
    @Transactional
    public List<BancoDtoResponse> getAll() {
    	return this.bancoDao.findAll().stream()
                .map(banco -> this.modelToResponse(banco))
                .collect(Collectors.toList());
    }

    /**
     * 📌 Método: getAllPaginated
     * 
     * 🧾 Descripción:
     * Lista bancos de forma paginada.
     * (Consistente con AreaServiceImpl)
     * 
     * @param pageable configuración de paginación
     * @return Page<BancoDtoResponse>
     */
    @Transactional
    public Page<BancoDtoResponse> getAllPaginated(Pageable pageable) {
    	return this.bancoDao.findAll(pageable)
                .map(banco -> this.modelToResponse(banco));
    }

    /**
     * 📌 Método: findByNombre
     * 
     * 🧾 Descripción:
     * Busca un banco por nombre.
     * 
     * @param nombre nombre del banco
     * @return BancoDtoResponse
     */
    @Transactional
    public BancoDtoResponse findByNombre(String nombre) {
        String nombreNormalizado = this.personalizer.normalizer(nombre);

        BancoModel banco = this.bancoDao.findByNombre(nombreNormalizado)
                .orElseThrow(() -> new RuntimeException("Banco no encontrado: " + nombre));

        return this.modelToResponse(banco);
    }

    /**
     * 📌 Método: delete
     * 
     * 🧾 Descripción:
     * Eliminación lógica (si tienes campo estado).
     * Si no, puedes cambiar a delete físico.
     * 
     * @param id ID del banco
     */
    @Transactional
    public void delete(Long id) {
        BancoModel banco = bancoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Banco no encontrado"));

        banco.setEstado(false); // requiere campo en BaseEntity
        this.bancoDao.save(banco);
    }

    /**
     * 📌 Método: updateNombre
     * 
     * 🧾 Descripción:
     * Actualiza el nombre de un banco.
     * 
     * @param id ID del banco
     * @param nuevoNombre nuevo nombre
     * @return BancoDtoResponse
     */
    @Transactional
    public BancoDtoResponse updateNombre(BancoDtoResponse response) {

        BancoModel banco = this.bancoDao.findById(response.getId())
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        String nombreNormalizado = this.personalizer.normalizer(response.getNombre());

        if (existsByNombre(nombreNormalizado)) {
            throw new RuntimeException("Ya existe un área con ese nombre");
        }

        banco.setNombre(nombreNormalizado);

        this.bancoDao.save(banco);

        return this.modelToResponse(banco);
    }
    
    /**
     * 📌 Actualización completa (usar con cuidado)
     */
    @Transactional
    public BancoDtoResponse update(BancoDtoResponse response) {

        
        String nombre = this.personalizer.normalizer(response.getNombre());

        BancoModel banco = new BancoModel();

        banco.setId(response.getId());
        banco.setNombre(nombre);
        banco.setEstado(response.getEstado());

        this.bancoDao.save(banco);

        return this.modelToResponse(banco);
    }

    // =========================
    // 🔧 MÉTODOS PRIVADOS
    // =========================
    
    
    /**
     * 📌 Verifica existencia por nombre
     */
    public boolean existsByNombre(String nombre) {
        return this.bancoDao.findByNombre(nombre).isPresent();
    }
    
    private BancoDtoResponse modelToResponse(BancoModel banco) {
    	return(this.modelMapper.map(banco, BancoDtoResponse.class));
    }
    
    private BancoModel responseToModel(BancoDtoResponse response) {
    	return(this.modelMapper.map(response, BancoModel.class));
    }
    
    private BancoModel requestToModel(BancoDtoRequest resquest) {
    	return(this.modelMapper.map(resquest, BancoModel.class));
    }
}
