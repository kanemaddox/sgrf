package com.saims.sgrf.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.enums.Moneda;

import lombok.Data;


public class SrfDto {
	
	@Data
	public static class Request{
		private String fechaSolicitud;
		private String fechaEstimada;
		private BigDecimal monto;
		private Moneda moneda;
		private String detalle;
		private String campoTexto;
		private String campoNumerico;
		private String tipo;
		private String cargo;
		private String codigo;
		private String centrocosto;
		private Long idPersona;
		private Map<String, Object> formato;	
	}
	
	@Data
	public static class Response{
		private Long id;
		private String numero;
		private int gestion;
		private String fechaSolicitud;
		private LocalDateTime fechaEstimada;
		private BigDecimal monto;
		private Moneda moneda;
		private String detalle;
		private String campoTexto;
		private String campoNumerico;
		private String tipo;
		private Estado estado;
		private String cargo;
		private String centrocosto;
		private String codigo;
		private boolean edit;
		private boolean pdf;
		private CuentaBancariaDto.Response cuenta;
		private EmpleadoDto.Response empleado;
		private List<AprobacionDto.Response> aprobadores;
		private Map<String, Object> formato;
		private String token;
	}
	
	@Data
	public static class Pendiente{
		private Long id;
		private String numero;
		private int gestion;
		private String fechaSolicitud;
		private double monto;
		private Moneda moneda;
		private String detalle;
		private Estado estado;
		private EmpleadoDto.Persona empleado;
	}
	
	@Data
	public static class Revisar{
		private Long id;
		private String numero;
		private int gestion;
		private String fechaSolicitud;
		private LocalDateTime fechaEstimada;
		private double monto;
		private Moneda moneda;
		private String detalle;
		private String campoTexto;
		private String campoNumerico;
		private String tipo;
		private Estado estado;
		private String cargo;
		private String centrocosto;
		private String codigo;
		private EmpleadoDto.Persona empleado;
		private CuentaBancariaDto.Srf cuenta;
		private List<AprobacionDto.Autorizador> autorizador;
		private Map<String, Object> formato;	
	}
	@Data
	public static class Detalle{
		private Long id;
		private String numero;
		private int gestion;
		private String fechaSolicitud;
		private LocalDateTime fechaEstimada;
		private BigDecimal monto;
		private Moneda moneda;
		private String detalle;
		private String campoTexto;
		private String campoNumerico;
		private String tipo;
		private Estado estado;
		private String cargo;
		private String centrocosto;
		private String codigo;
		private String idp;
		private String nombre;
		private String email;
		private boolean edit;
		private boolean pdf;
		private CuentaBancariaDto.Response cuenta;
		private EmpleadoDto.Response empleado;
		private List<AprobacionDto.Detalle> aprobadores;
		private Map<String, Object> formato;
		private String token;
	}
	@Data
	public static class Publico{
		private Long id;
		private String numero;
		private int gestion;
		private String fechaSolicitud;
		private LocalDateTime fechaEstimada;
		private BigDecimal monto;
		private Moneda moneda;
		private String detalle;
		private String campoTexto;
		private String campoNumerico;
		private Estado estado;
		private String codigo;
		private Map<String, Object> formato;	
	}
	@Data
	public static class Stado{
		private Long id;
		private String numero;
		private int gestion;
		private String fechaSolicitud;
		private LocalDateTime fechaEstimada;
		private BigDecimal monto;
		private Moneda moneda;
		private String detalle;
		private String campoTexto;
		private String campoNumerico;
		private String tipo;
		private Estado estado;
		private String token;
		private String fechaTitulo;
		private String montoTitulo;
		private String idp;
		private double aprobacion;
	}
}
