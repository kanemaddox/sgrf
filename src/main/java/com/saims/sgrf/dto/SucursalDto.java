package com.saims.sgrf.dto;

import lombok.Data;


public class SucursalDto {
	
	@Data
	public static class Request{
		private String nombre;
		private String codigo;
		private String prefijo;	
	}
	
	@Data
	public static class Response{
		private Long id;
		private String nombre;
		private String codigo;
		private String prefijo;
		private String fechaCreacion;
		private String fechaActualizacion;
	}
}
