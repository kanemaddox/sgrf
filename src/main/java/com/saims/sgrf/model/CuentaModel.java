package com.saims.sgrf.model;

import com.saims.sgrf.base.BaseEntity;
import com.saims.sgrf.enums.Estado;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "cuenta")
public class CuentaModel extends BaseEntity{

	@Column(name = "tipo")
	private String tipo;
	
	@Column(name = "cuenta")
	private String cuenta;
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "glosa")
	private String glosa;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull
	private Estado categoria;
	
}
