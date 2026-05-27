package com.saims.sgrf.dto;

import lombok.Data;

public class PersonaDto {
	
	/**
	 * Datos para la Tabla persona
	 */
	@Data
	public static class Request{
		private String idp;
		private String nombre;
		private String paterno;
		private String materno;
		private String email;
		private String fechaNacimiento;
		private Boolean sexo;
		private String celular;
	}
	
	@Data
	public static class Response{
		private Long id;
		private String idp;
		private String nombre;
		private String paterno;
		private String materno;
		private String email;
		private String fechaNacimiento;
		private Boolean sexo;
		private String celular;
		private String foto;
		private String fechaCreacion;
		private String fechaActualizacion;
	}
	
	@Data
	public static class Profesion{
		private Long id;
		private String profesion;
		private boolean estado;
		private String fechaCreacion;
		private String fechaActualizacion;
	}
	
	@Data
	public static class Pendiente{
		private String idp;
		private String nombre;
		private String paterno;
		private String materno;
		private String foto;
	}
}
