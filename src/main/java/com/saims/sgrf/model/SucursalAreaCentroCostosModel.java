package com.saims.sgrf.model;

import com.saims.sgrf.base.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 📌 Clase: SucrusalCentroCostosModel
 * 
 * 🧾 Descripción:
 * Relacion entre Sucursal y Centro de Costos.
 * 
 */
@Getter // Genera getters automáticamente
@Setter // Genera setters automáticamente
@AllArgsConstructor // Constructor con todos los atributos
@NoArgsConstructor  // Constructor vacío

@Entity // Indica que es una entidad JPA
@Table(name = "sucursal_area_centrocostos") // Nombre de la tabla en la BD
public class SucursalAreaCentroCostosModel extends BaseEntity{
	
	/**
     * 👤 Relación con Sucursal
     */
    @ManyToOne
    @JoinColumn(name = "id_sucursal", nullable = false)
    private SucursalModel sucursal;
    
    /**
     * 👤 Relación con Area
     */
    @ManyToOne
    @JoinColumn(name = "id_area", nullable = false)
    private AreaModel area;
    
    /**
     * 👤 Relación con Centro de Costos
     */
    @ManyToOne(optional=true)
    @JoinColumn(name = "id_centrocostos", nullable = true)
    private CentroCostosModel centrocostos;

}
