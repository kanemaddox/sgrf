package com.saims.sgrf.dto;

import java.math.BigDecimal;

import com.saims.sgrf.enums.Estado;

import lombok.Data;

public class CuentaDto {
	
	@Data
	public static class Request{
		private String tipo;
		private String cuenta;
		private String nombre;
		private String glosa;
		private Estado categoria;
	}
	
	@Data
	public static class Response{
		private Long idCuenta;
		private String tipo;
		private String cuenta;
		private String nombre;
		private String glosa;
		private Estado categoria;
	}
	
	@Data
	public static class Resumen{
		private String tipo;
		private String cuenta;
		private String nombre;
		private String glosa;
		private Estado categoria;
		private BigDecimal total;
	}


}
