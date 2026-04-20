package com.saims.sgrf.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.SucursalDao;
import com.saims.sgrf.dto.DatosDtoRequest;
import com.saims.sgrf.dto.SucursalDtoResponse;
import com.saims.sgrf.model.SucursalModel;
import com.saims.sgrf.service.SucursalService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SucursalServiceImpl implements SucursalService {

    private final SucursalDao sucursalDao;
    private final ModelMapper modelMapper;
    private Personalizer personalizer = new Personalizer();

    // =========================
    // 🟢 CREATE
    // =========================

    /**
     * 📌 Crea una sucursal evitando duplicados por código
     * 
     * ✔ Convierte el código a mayúsculas y elimina espacios
     * ✔ Si la sucursal ya existe, la retorna
     * ✔ Si no existe, crea una nueva sucursal
     */
    @Transactional
    public SucursalModel createOrGet(DatosDtoRequest datosDtoRequest) {

        String codigoSucursal = this.personalizer.normalizer(datosDtoRequest.getCodigoSucursal());

        return this.sucursalDao.findByCodigo(codigoSucursal)
                .orElseGet(() -> {
                    SucursalModel nuevaSucursal = new SucursalModel();
                    nuevaSucursal.setCodigo(codigoSucursal);
                    nuevaSucursal.setNombre(this.personalizer.normalizer(datosDtoRequest.getNombreSucursal()));
                    nuevaSucursal.setPrefijo(this.personalizer.generatePrefijo(codigoSucursal));
                    return sucursalDao.save(nuevaSucursal);
                });
    }

    /**
     * 📌 Crea una nueva sucursal (sin validación de duplicados)
     * ⚠ Usar solo cuando se tenga certeza de que no existe
     */
    @Transactional
    public SucursalDtoResponse create(SucursalDtoResponse response) {
    	SucursalModel sucursal = new SucursalModel();
    	sucursal.setNombre(this.personalizer.normalizer(response.getNombre()));
    	sucursal.setCodigo(this.personalizer.normalizer(response.getCodigo()));
    	sucursal.setPrefijo(this.personalizer.generatePrefijo(response.getPrefijo()));
    	this.sucursalDao.save(sucursal);
        return (this.modelToResponse(sucursal));
    }

    // =========================
    // 🔵 READ
    // =========================

    /**
     * 📌 Obtiene una sucursal por ID
     */
    @Transactional
    public SucursalDtoResponse getSucursal(Long id) {
        
    	SucursalModel sucursal =  this.sucursalDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: " + id));
        
        return (this.modelToResponse(sucursal));
    }
    

    /**
     * 📌 Obtiene todas las sucursales
     */
    public List<SucursalDtoResponse> getAll() {
    	List<SucursalDtoResponse> sucursales = this.sucursalDao.findAll().stream()
    			.map(sucursal->this.modelToResponse(sucursal))
    			.collect(Collectors.toList());
        return sucursales;
    }

    /**
     * 📌 Busca una sucursal por código
     */
    public SucursalDtoResponse getByCodigo(String codigo) {
    	SucursalModel sucursal = sucursalDao.findByCodigo(this.personalizer.normalizer(codigo))
        .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con código: " + codigo));
        return this.modelToResponse(sucursal) ;
    }
    /**
     * 📌 Búsqueda por nombre
     */
    @Transactional
    public SucursalDtoResponse findByNombre(String nombre) {

        SucursalModel sucursal = this.sucursalDao.findByNombre(this.personalizer.normalizer(nombre))
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        return this.modelToResponse(sucursal);
    }

    /**
     * 📌 Verifica si existe una sucursal por código
     */
    public boolean existsByCodigo(String codigo) {
        return this.sucursalDao.findByCodigo(this.personalizer.normalizer(codigo)).isPresent();
    }

    // =========================
    // 🟡 UPDATE
    // =========================

    /**
     * 📌 Actualiza los datos de una sucursal
     */
    @Transactional
    public SucursalDtoResponse update(SucursalDtoResponse response) {

        SucursalModel sucursal = getId(response.getId());

        sucursal.setNombre(this.personalizer.normalizer(response.getNombre()));
        sucursal.setCodigo(this.personalizer.normalizer(response.getCodigo()));
        sucursal.setPrefijo(this.personalizer.generatePrefijo(response.getPrefijo()));
        this.sucursalDao.save(sucursal);

        return this.modelToResponse(sucursal);
    }

    // =========================
    // 🔴 DELETE
    // =========================

    /**
     * 📌 Elimina una sucursal por ID
     */
    @Transactional
    public void delete(Long id) {
        SucursalModel sucursal = getId(id);
        this.sucursalDao.delete(sucursal);
    }
    
    // =========================
    // 🔴 Metodos Auxiliares
    // =========================
    
    private SucursalDtoResponse modelToResponse(SucursalModel sucursal) {
    	return (this.modelMapper.map(sucursal, SucursalDtoResponse.class));
    }
    
    private SucursalModel getId(Long id) {
        return this.sucursalDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: " + id));
    }

}