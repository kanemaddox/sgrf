package com.saims.sgrf.dto;

import lombok.Data;

@Data
public class CargoDto {
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
