package com.saims.sgrf.model;

import com.saims.sgrf.base.StatusEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter // Genera getters automáticamente
@Setter // Genera setters automáticamente
@AllArgsConstructor // Constructor con todos los atributos
@NoArgsConstructor  // Constructor vacío

@Entity 
@Table(name = "empleado_sucursal")
public class EmpleadoSucursalModel extends StatusEntity {
	
	/**
     * 👤 Relación con Empleado
     * muchos empleado -> sucursal
     */
    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    private EmpleadoModel empleado;
    
    /**
     * 👤 Relación con Sucursal
     * muchas sucursales -> empleado
     */
    @ManyToOne
    @JoinColumn(name = "id_sucursal", nullable = false)
    private SucursalModel sucursal;
    
    /**
     * 👤 Relación con Centro Costos
     * muchas centro costos -> empleado
     */
    @ManyToOne
    @JoinColumn(name = "id_centrocostos", nullable = false)
    private CentroCostosModel centrocostos;
    
    /**
     * 👤 Relación con Centro Costos
     * muchas area -> empleado
     */
    @ManyToOne
    @JoinColumn(name = "id_area", nullable = false)
    private AreaModel area;
    

}
