package com.saims.sgrf.dto;

import lombok.Data;


public class AreaDto {
	
	
	@Data
	public static class Request{
		private String nombre;
		private boolean estado;	
	}
	
	@Data
	public static class Response{
		private Long id;
		private String nombre;
		private Boolean estado;
		private String fechaCreacion;
		private String fechaActualizacion;
	}
	
	
	
}
