package com.saims.sgrf.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.enums.Moneda;
import lombok.Data;


public class SrfDto {
	
	@Data
	public static class Request{
		private String fechaSolicitud;
		private String fechaEstimada;
		private double monto;
		private Moneda moneda;
		private String detalle;
		private String campoTexto;
		private String campoNumerico;
		private String tipo;
		private String cargo;
		private String codigo;
		private String centrocosto;
		private Long idUsuario;
	}
	
	@Data
	public static class Response{
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
		private boolean edit;
		private boolean pdf;
		private CuentaBancariaDto.Response cuenta;
		private EmpleadoDto.Response empleado;
		private List<AprobacionDto.Response> aprobadores;
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
	}
		
}
