package com.saims.sgrf.dto;

import lombok.Data;


public class CuentaBancariaDto {

	@Data
	public static class Request{
		private String numero;
		private String tipoCuenta;
		private boolean estado;
		private Long idPersona;
		private Long idBanco;
	}
	
	@Data
	public static class Response{
		private Long id;
		private String numero;
		private String tipoCuenta;
		private Boolean estado;
		private Long idPersona;
		private BancoDto.Response banco;
		private String fechaCreacion;
		private String fechaActualizacion;
	}
	
	@Data
	public static class Srf{
		private Long id;
		private String numero;
		private String tipoCuenta;
		private Boolean estado;
		private BancoDto.Srf banco;
	}

}
