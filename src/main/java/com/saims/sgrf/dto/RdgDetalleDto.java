package com.saims.sgrf.dto;

import java.math.BigDecimal;
import java.util.Map;

import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.enums.Moneda;

import lombok.Data;

public class RdgDetalleDto {
	
	@Data
	public static class Request{
		private Long idDetalle;
		private String fecha;
		private String detalle;
		private BigDecimal importe;
		private Moneda moneda;
		private String documento;
		private Long idCuenta;
		private String archivo;
		private String numero;
		private String token;
		private Estado agregado;
	}
	
	@Data
	public static class Response{
		private Long id;
		private Long idDetalle;
		private String fecha;
		private String detalle;
		private BigDecimal importe;
		private Moneda moneda;
		private String documento;
		private CuentaDto.Response cuenta;
		private Long idCuenta;
		private String archivo;
		private String numero;
		private Estado agregado;
		private Map<String, Object> aprobado;
		private Estado estado;
		private String token;
	}
	
	@Data
	public static class Publico{
		private Long idDetalle;
		private String fecha;
		private BigDecimal importe;
		private Moneda moneda;
		private String documento;
		private String archivo;
		private Estado estado;
		private String token;
	}
}
