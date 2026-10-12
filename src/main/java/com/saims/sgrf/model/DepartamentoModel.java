package com.saims.sgrf.model;

import com.saims.sgrf.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "departamento")
public class DepartamentoModel extends BaseEntity{
	
	@Column(name = "nombre",nullable = false,unique = true)
	private String nombre;

}
