package com.saims.sgrf.dto;

import lombok.Data;


public class BancoDto {
	@Data
	public static class Request{
		private String Nombre;
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
	
	@Data
	public static class Srf{
		private String nombre;
	}
	

}
