package com.saims.sgrf.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.sgrf.dao.SucursalAreaCentroCostosDao;
import com.saims.sgrf.dto.SucursalAreaCentroCostosDtoResponse;
import com.saims.sgrf.model.AreaModel;
import com.saims.sgrf.model.CentroCostosModel;
import com.saims.sgrf.model.SucursalAreaCentroCostosModel;
import com.saims.sgrf.model.SucursalModel;
import com.saims.sgrf.service.SucursalAreaCentroCostosService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SucursalAreaCentroCostosServiceImpl implements SucursalAreaCentroCostosService {

    private final SucursalAreaCentroCostosDao sucursalAreaCentroCostosDao;
    private final ModelMapper modelMapper;

    // =========================
    // 🟢 CREATE
    // =========================

    /**
     * 📌 Crea una relación entre Sucursal, Área y Centro de Costos evitando duplicados.
     * 
     * 🔎 Lógica:
     * - Si ya existe la relación → la retorna
     * - Si no existe → la crea y guarda
     * 
     * ⚠️ Consideraciones:
     * - Centro de costos puede ser null
     * - Se valida por combinación única de IDs
     * 
     * @param sucursalModel entidad sucursal
     * @param areaModel entidad área
     * @param centroCostosModel entidad centro de costos (opcional)
     * @return SucursalAreaCentroCostosModel existente o creado
     */
    @Transactional
    public SucursalAreaCentroCostosModel createOrGet(SucursalModel sucursalModel,AreaModel areaModel,CentroCostosModel centroCostosModel) {
        
    	Long centroCostosId = (centroCostosModel != null)? centroCostosModel.getId(): null;
    	
    	SucursalAreaCentroCostosModel model = this.sucursalAreaCentroCostosDao.getSucursalAreaCentroCostos(sucursalModel.getId(),areaModel.getId(),centroCostosId)
                .orElseGet(() -> {
                    SucursalAreaCentroCostosModel nuevo = new SucursalAreaCentroCostosModel();
                    nuevo.setSucursal(sucursalModel);
                    nuevo.setArea(areaModel);
                    nuevo.setCentrocostos(centroCostosModel);
                    return this.sucursalAreaCentroCostosDao.save(nuevo);
                });
        return model;
    }

    // =========================
    // 🔵 READ
    // =========================

    /**
     * 📌 Obtiene una relación por ID
     */
    
    public SucursalAreaCentroCostosModel getById(Long id) {
        return this.sucursalAreaCentroCostosDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Relación no encontrada con ID: " + id));
    }

    /**
     * 📌 Lista todas las relaciones
     */
    public List<SucursalAreaCentroCostosDtoResponse> getAll() {
    	List<SucursalAreaCentroCostosDtoResponse>values = this.sucursalAreaCentroCostosDao.findAll().stream()
    			.map(value->this.modelToResponse(value))
    			.collect(Collectors.toList());
    	
    	  return values;
    }
    
    public List<SucursalAreaCentroCostosModel>findAll (){
    	//return this.sucursalAreaCentroCostosDao.getSucursalesAreasCentroCostos();
    	return this.sucursalAreaCentroCostosDao.findAll();
    }

    /**
     * 📌 Obtiene relaciones por sucursal
     */
    public List<SucursalAreaCentroCostosModel> getBySucursal(Long sucursalId) {
        return this.sucursalAreaCentroCostosDao.findBySucursalId(sucursalId);
    }

    /**
     * 📌 Obtiene relaciones por área
     */
    public List<SucursalAreaCentroCostosModel> getByArea(Long areaId) {
        return this.sucursalAreaCentroCostosDao.findByAreaId(areaId);
    }

    /**
     * 📌 Obtiene relaciones por centro de costos
     */
    
    public List<SucursalAreaCentroCostosModel> getByCentroCostos(Long centroCostosId) {
        return this.sucursalAreaCentroCostosDao.findByCentrocostosId(centroCostosId);
    }

    // =========================
    // 🟡 VALIDATIONS
    // =========================

    /**
     * 📌 Verifica si existe una relación específica
     */
    
    public boolean exists(Long sucursalId,Long areaId,Long centroCostosId) {

        return this.sucursalAreaCentroCostosDao
                .getSucursalAreaCentroCostos(sucursalId, areaId, centroCostosId)
                .isPresent();
    }

    // =========================
    // 🟠 UPDATE
    // =========================

    /**
     * 📌 Actualiza una relación existente
     */
    @Transactional
    public SucursalAreaCentroCostosModel update(Long id,SucursalModel sucursalModel,AreaModel areaModel,CentroCostosModel centroCostosModel) {

        SucursalAreaCentroCostosModel entity = getById(id);

        entity.setSucursal(sucursalModel);
        entity.setArea(areaModel);
        entity.setCentrocostos(centroCostosModel);

        return this.sucursalAreaCentroCostosDao.save(entity);
    }

    // =========================
    // 🔴 DELETE
    // =========================

    /**
     * 📌 Elimina una relación por ID
     */
    @Transactional
    public void delete(Long id) {
        if (!this.sucursalAreaCentroCostosDao.existsById(id)) {
            throw new RuntimeException("No existe la relación con ID: " + id);
        }
        this.sucursalAreaCentroCostosDao.deleteById(id);
    }
    
    private SucursalAreaCentroCostosDtoResponse modelToResponse(SucursalAreaCentroCostosModel value) {
    	return (this.modelMapper.map(value, SucursalAreaCentroCostosDtoResponse.class));
    }

}
