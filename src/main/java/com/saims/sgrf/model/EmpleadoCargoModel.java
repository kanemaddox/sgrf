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
@Table(name = "empleado_cargo")
public class EmpleadoCargoModel extends StatusEntity{
		
	/**
     * 👤 Relación con Empleado
     * muchos cargos -> empleado
     */
    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    private EmpleadoModel empleado;
    
    /**
     * 👤 Relación con Cargo
     * muchos empleado -> cargo
     */
    @ManyToOne
    @JoinColumn(name = "id_cargo", nullable = false)
    private CargoModel cargo;
    
    @ManyToOne
    @JoinColumn(name="idp_jefe", nullable = false)
    private PersonaModel jefe;
	
}
