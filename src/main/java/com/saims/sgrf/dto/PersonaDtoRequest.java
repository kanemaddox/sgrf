package com.saims.sgrf.dto;

import lombok.Data;

@Data
public class PersonaDtoRequest {
	
	/**
	 * Datos para la Tabla persona
	 */
	private String idp;
	private String nombre;
	private String paterno;
	private String materno;
	private String email;
	private String fechaNacimiento;
	private Boolean sexo;
	private String celular;

}
