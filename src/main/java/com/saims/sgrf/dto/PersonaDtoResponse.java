package com.saims.sgrf.dto;

import lombok.Data;

@Data
public class PersonaDtoResponse {
	
	/**
	 * Datos para el Json
	 */
	private Long id;
	private String idp;
	private String nombre;
	private String paterno;
	private String materno;
	private String email;
	private String fechaNacimiento;
	private Boolean sexo;
	private String celular;
	private String fechaCreacion;
	private String fechaActualizacion;

}
