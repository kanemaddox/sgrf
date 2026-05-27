package com.saims.sgrf.dto;

import lombok.Data;

public class CentroCostosDto {

	@Data
	public static class Request {
		private String codigo;
		private String nombre;
		private String prefijo;
		private boolean estado;
	}

	@Data
	public static class Response {
		private Long id;
		private String nombre;
		private String codigo;
		private String prefijo;
		private Boolean estado;
		private String fechaCreacion;
		private String fechaActualizacion;
	}

}
