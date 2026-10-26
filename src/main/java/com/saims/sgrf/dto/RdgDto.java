package com.saims.sgrf.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.enums.Moneda;

import lombok.Data;

public class RdgDto {
	
	@Data
	public static class Request{
		
		private Long idPersona;
		private String actividad;
		private String fecha;
		private String al;
		private String del;
		private String detalle;
		private String area;
		
		private String cargo;
		private String centrocostos;
		private String codigo;

		private Estado estado;
		private Map<String, Object> formato;
		private BigDecimal devolver;
		private BigDecimal gasto;
		private BigDecimal importe;
		private BigDecimal rembolso;
		private BigDecimal saldo;
		
		private BigDecimal tc;
		private Moneda moneda;
		private String participantes;
		private String tipoCuenta;
		private String cuenta;
		private String banco;
		private List<RdgDetalleDto.Request> factura;
	}
	
	
	@Data
	public static class Srf{
		private Long id;
		private String token;
		private String actividad;
		private String fechaSolicitud;
		private String fechaCreacion;
		private String numero;
		private int gestion;
		private BigDecimal importe;
		private Moneda moneda;
		private Estado estado;
	}
	
	@Data
	public static class Lista{
		private Long id;
		private String detalle;
		private String numero;
		private String idp;
		private int gestion;
		private BigDecimal importe;
		private BigDecimal gasto;
		private BigDecimal rembolso;
		private BigDecimal saldo;
		private BigDecimal devolver;
		private Moneda moneda;
		private Estado estado;
		private String fecha;
		private String token;
		private String actividad;
	}
	
	@Data
	public static class ListaRevisar{
		private Long idAprobacion;
		private Long id;
		private String detalle;
		private String numero;
		private int gestion;
		private BigDecimal importe;
		private BigDecimal gasto;
		private BigDecimal rembolso;
		private BigDecimal saldo;
		private BigDecimal devolver;
		private Moneda moneda;
		private Estado estado;
		private String fecha;
		
		private String nombre;
		private String foto;
		private String idp;
		private String token;
	}
	
	@Data
	public static class Response{
		private Long idRdg;
		private Long idSrf;
		private Long idPersona;
		
		private String actividad;
		private String fecha;
		private String al;
		private String del;
		private String detalle;
		private String area;
		
		private String cargo;
		private String centrocostos;
		private String codigo;

		private Estado estado;
		private Map<String, Object> formato;
		private BigDecimal devolver;
		private BigDecimal gasto;
		private BigDecimal importe;
		private BigDecimal rembolso;
		private BigDecimal saldo;
		
		private BigDecimal tc;
		private Moneda moneda;
		private String participantes;
		private String tipoCuenta;
		private String cuenta;
		private String banco;
		private List<RdgDetalleDto.Request> factura;
		private List<RdgDevolverDto.Request>deposito;
		
	}
	
	@Data
	public static class Actividad{
		private Long idRdg;
		private Long idSrf;
		private String actividad;
		private String al;
		private String del;
		private String detalle;
		private Moneda moneda;
		private String participantes;
	}
	
	@Data
	public static class Solicitud{
		private Long idRdg;
		private Long idSrf;
		private Long idPersona;
		private Estado estado;
		private String actividad;
		private String al;
		private String del;
		private String detalle;
		private Moneda moneda;
		private String participantes;
		
		private BigDecimal devolver;
		private BigDecimal gasto;
		private BigDecimal rembolso;
		private BigDecimal saldo;
		private String token;
		private List<RdgDetalleDto.Response> factura;
		
	}
	
	@Data
	public static class Detalle{
		private Long idRdg;
		private Long idSrf;
		private String token;
		
		private String numero;
		private int gestion;
		private String nombre;
		private String idp;

		private String actividad;
		private String fecha;
		private String al;
		private String del;
		private String detalle;
		private String area;
		
		private String cargo;
		private String centrocostos;
		private String codigo;

		private Estado estado;
		private Map<String, Object> formato;
		private BigDecimal devolver;
		private BigDecimal gasto;
		private BigDecimal importe;
		private BigDecimal rembolso;
		private BigDecimal saldo;
		
		private BigDecimal tc;
		private Moneda moneda;
		private String participantes;
		private String tipoCuenta;
		private String cuenta;
		private String banco;
		private List<RdgDetalleDto.Response> factura;
		private List<AprobacionDto.RdgResponse> aprobadores;
		private List<CuentaDto.Resumen>resumen;
		private List<RdgDevolverDto.Response>deposito;
		private boolean pdf;
		private boolean editar;
		private boolean devuelto;
	}
	
	@Data
	public static class CreateRdg{
		private Long idRdg;
		private String actividad;
		private String al;
		private String del;
		private String detalle;
		private BigDecimal devolver;
		private Estado estado;
		private String fecha;
		private Map<String, Object> formato;
		private BigDecimal gasto;
		private int gestion;
		private String numero;
		
		private Long idSrf;
		private String numeroSrf;
		private int gestionSrf;
		private BigDecimal importe;
		private Moneda moneda;
		
		private String participantes;
		private BigDecimal rembolso;
		private BigDecimal saldo;
		private BigDecimal tc;
		
		private String nombre;
		private String idp;
		private String cargo;
		private String area;
		private String tipo;
		private String cuenta;
		private String banco;
		private String centrocostos;
		private String codigo;
		private List<CuentaDto.Response> cuentaGasto;
		private List<RdgDetalleDto.Response> factura;
	}
	
	@Data
	public static class Revisar{
		private Long idRdg;
		private Long idSrf;
		
		private Long idAprobacion;
		private Estado estadoAprobacion;
		private int destinoAprobacion;
		
		private String numero;
		private int gestion;

		private String actividad;
		private String fecha;
		private String al;
		private String del;
		private String detalle;
		private String area;
		
		private String cargo;
		private String centrocostos;
		private String codigo;
		private String nombre;
		private String idp;

		private Estado estado;
		private Map<String, Object> formato;
		private BigDecimal devolver;
		private BigDecimal gasto;
		private BigDecimal importe;
		private BigDecimal rembolso;
		private BigDecimal saldo;
		
		private BigDecimal tc;
		private Moneda moneda;
		private String participantes;
		private String tipoCuenta;
		private String cuenta;
		private String banco;
		
		private List<RdgDetalleDto.Response> factura;
		private List<AprobacionDto.RdgResponse> aprobadores;
		private List<CuentaDto.Resumen>resumen;
		private List<RdgDevolverDto.Response>deposito;
	}
	
	@Data
	public static class Publico{
		private Long id;
		private Long idSrf;
		private String token;
		
		private String numero;
		private int gestion;
		private String nombre;

		private String actividad;
		private String fecha;
		private String al;
		private String del;
		private String detalle;
		private String area;
		
		private String cargo;
		private String centrocostos;
		private String codigo;

		private Estado estado;
		private Map<String, Object> formato;
		private BigDecimal devolver;
		private BigDecimal gasto;
		private BigDecimal importe;
		private BigDecimal rembolso;
		private BigDecimal saldo;
		
		private BigDecimal tc;
		private Moneda moneda;
		private String participantes;
		private String tipoCuenta;
		private String cuenta;
		private String banco;
		private List<RdgDetalleDto.Response> factura;
		private List<AprobacionDto.RdgResponse> aprobadores;
		private List<CuentaDto.Resumen>resumen;
	}
}
