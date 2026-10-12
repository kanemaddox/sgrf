package com.saims.sgrf.dto;

import java.math.BigDecimal;

import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.enums.Moneda;

import lombok.Data;

public class RdgDevolverDto {
	
	@Data
	public static class Request{
		private Long idDevolver;
		private String fecha;
		private String detalle;
		private BigDecimal importe;
		private BigDecimal gasto;
		private BigDecimal deposito;
		private Moneda moneda;
		private String documento;
		private String archivo;
		private String numero;
		private String token;
		private Estado agregado;
	}
	@Data
	public static class Response{
		private Long id;
		private String fecha;
		private String detalle;
		private BigDecimal importe;
		private BigDecimal gasto;
		private BigDecimal deposito;
		private Moneda moneda;
		private String documento;
		private String archivo;
		private String numero;
		private String token;
		private Estado agregado;
	}

}
