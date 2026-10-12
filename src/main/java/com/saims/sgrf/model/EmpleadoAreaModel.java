package com.saims.sgrf.model;

import com.saims.sgrf.base.StatusEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor  

@Entity 
@Table(name = "empleado_area")
public class EmpleadoAreaModel extends StatusEntity{
	
	@ManyToOne
	@JoinColumn(name = "id_empleado", nullable = false)
	private EmpleadoModel empleado;
	
	@ManyToOne
	@JoinColumn(name = "id_area", nullable = false)
	private AreaModel area;
	
}
