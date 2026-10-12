package com.saims.sgrf.dto;

import java.util.List;

import com.saims.sgrf.enums.TipoArea;

import lombok.Data;


public class AreaDto {
	
	
	@Data
	public static class Request{
		private int nivel;
		private String nombre;
		private String codigo;	
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
	public static class Sub{
		private Long idCosto;
		private String prefijo;
		private String codigo;
		private String departamento;
		private TipoArea tipo;
	}
	
	@Data
	public static class Costo{
		private Long idCosto;
		private String prefijo;
		private String codigo;
		private String departamento;
		private TipoArea tipo;
		private List<AreaDto.Sub>sub;
	}
	
	@Data
	public static class Estructura{
		private Long idEstructura;
		private String prefijo;
		private String codigo;
		private String departamento;
		private TipoArea tipo;
		private List<AreaDto.Costo>costo;
	}
	
	
	
}
