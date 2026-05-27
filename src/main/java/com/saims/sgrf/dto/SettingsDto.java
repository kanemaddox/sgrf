package com.saims.sgrf.dto;

import java.util.Map;

import lombok.Data;


public class SettingsDto {

	@Data
	public static class Request{
		private String categoria;
	    private String nombre;
	    private Map<String, Object> value;	
	}
	
	@Data
	public static class Response{
		private Long id;
		private String categoria;
	    private String nombre;
	    private Map<String, Object> value;
	    private String fechaCreacion;
	    private String fechaActualizacion;
	}
	
}
