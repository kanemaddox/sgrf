package com.saims.sgrf.dto;

import com.saims.sgrf.enums.Estado;

import lombok.Data;


public class AprobacionDto {

	@Data
	public static class Response{
		private Long id;
		private int destino;
		private Estado estado;
		private String tipo;
		private String fechaActualizacion;
		private PersonaDto.Response jefe;
		private SrfDto.Response srf;
		private String obs;
	}
	
	@Data
	public static class Pendiente{
		private Long id;
		private int destino;
		private Estado estado;
		private String tipo;
		private String fechaCreacion;
		private String fechaActualizacion;
		private SrfDto.Pendiente srf;
	}
	
	@Data
	public static class Revisar{
		private Long id;
		private int destino;
		private Estado estado;
		private String tipo;
		private String fechaCreacion;
		private String fechaActualizacion;
		private SrfDto.Revisar srf;
	}
	
	@Data
	public static class Autorizador{
		private Long id;
		private int destino;
		private Estado estado;
		private String tipo;
		private PersonaDto.Pendiente persona;	
	}
	
	@Data
	public static class UpdateRequest{
		private Long id;
		private int destino;
		private Estado estado;
		private String tipo;
		private Long idPersona;
		private Long idSrf;
		private String obs;
	}
	
}
