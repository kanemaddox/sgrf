package com.saims.sgrf.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.enums.Moneda;

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
	public static class Stado{
		private Long id;
		private String token;
		private int destino;
		private Estado estado;
		private String tipo;
		private String fechaCreacion;
		private String fechaActualizacion;
		
		private String nombre;
		private String idp;
		private String foto;
		
		private String numero;
		private int gestion;
		private String fechaSolicitud;
		private LocalDateTime fechaEstimada;
		private BigDecimal monto;
		private Moneda moneda;
		private String detalle;
		private String campoTexto;
		private String campoNumerico;
		private String tokenSrf;
		private String fechaTitulo;
		private String montoTitulo;
	}
	
	@Data
	public static class Revisar{
		private Long id;
		private String token;
		private int destino;
		private Estado estado;
		private String tipo;
		private String obs;
		private String fechaCreacion;
		private String fechaActualizacion;
		
		//formato
		private String titulo;
		private String seccionUno;
		private String seccionDos;
		private String fechaTitulo;
		private String montoTitulo;
		private String descripcion;
		private Boolean estadoComplemento;
		private String complemento;
		private String detalleComplemento;
		private String cantidadComplemento;
		private Boolean trueComplemento;
		
		private int cantidadSolicitudes;
		
		private String seccionTres;
		private Boolean estadoTres;
		private String detalleFormato;
		
		//empleado
		private String nombre;
		private String idp;
		private String cargo;
		private String codigo;
		private String centro;
		
		// srf
		private Long idSrf;
		private String numero;
		private int gestion;
		private String fechaSolicitud;
		private LocalDateTime fechaEstimada;
		private BigDecimal monto;
		private Moneda moneda;
		private String detalle;
		private String campoTexto;
		private String campoNumerico;
		private String tokenSrf;
		
		private List<AutorizadorLite>autorizador;
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
	public static class AutorizadorLite{
		private Long id;
		private int destino;
		private Estado estado;
		private String nombre;
		private String idp;
		private String foto;	
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
	
	@Data
	public static class Detalle{
		private Long id;
		private int destino;
		private Estado estado;
		private String tipo;
		private String fechaActualizacion;
		private PersonaDto.Response jefe;
		private String cargo;
		private SrfDto.Response srf;
		private String obs;
	}
	
	@Data
	public static class RdgResponse{
		private Long id;
		private int destino;
		private Estado estado;
		private String tipo;
		private String fechaActualizacion;
		private PersonaDto.Response persona;
		private String obs;
		private String cargo;
	}
	
	@Data
	public static class AprobacionRdg{
		private Long idAprobacion;
		private Long idPersona;
		private Long idRdg;
		private String obs;
		private Estado estado;
		private int destino;
		private List<RdgDetalleDto.Response>factura;
	}
	
	
}
