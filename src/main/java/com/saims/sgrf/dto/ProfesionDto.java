package com.saims.sgrf.dto;

import lombok.Data;


public class ProfesionDto {

	@Data
	public static class Request{
		private String nombre;	
	}
	
	@Data
	public static class Response{
		private Long id;
		private String nombre;
		private String fechaActualizacion;
		private String fechaCreacion;
	}
	

}
