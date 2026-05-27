package com.saims.sgrf.dto;

import java.util.List;
import java.util.Map;

import com.saims.sgrf.enums.Rol;

import lombok.Data;


public class DatosDto {
	
	@Data
	public static class Request{
		/**
		 * Datos para la Tabla persona
		 */
		private String idp;
		private String nombre;
		private String paterno;
		private String materno;
		private String email;
		
		/**
		 * Datos Para profesion  
		 */
		private String profesionNombre;
		
		/**
		 * Datos para cargo
		 */
		private String cargoNombre;
		
		/**
		 * Datos para Cuenta Bancaria
		 */
		private String numero;
		private String tipoCuenta;
		
		/**
		 * Datos para el Banco
		 */
		private String bancoNombre;
		
		
		
		/**
		 * Datos para Sucursal
		 */
		private String codigoSucursal;
		private String nombreSucursal;
		
		/**
		 * Datos para Centro de Costos
		 */
		private String codigoCentroCostos;
		private String nombreCentroCostos;
		
		/**
		 * Datos para Area
		 */
		private String nombreArea;
		
		/**
		 * Datos para el Jefe
		 */
		private String idpJefe;
		private Boolean tipo;	
	}
	
	@Data
	public static class Response{
		private Long idUsuario;
		private String imagen;
		private boolean estado;
		private Map<String, Object> pernicions;
		private Rol rol;
		/**
		 * Datos para la Tabla persona
		 */
		private Long idPersona;
		private String idp;
		private String nombre;
		private String apellido;
		private String email;
		private String celular;
		
		/**
		 * Datos Para profesion  
		 */
		private Long idProfesion;
		private String profesionNombre;
		
		/**
		 * Datos para cargo
		 */
		private Long idCargo;
		private String cargoNombre;
		private boolean estadoCargo;
		
		/**
		 * Datos para Cuenta Bancaria
		 */
		private Long idCuentaBancaria;
		private String numero;
		private String tipoCuenta;
		private boolean estadoCuentaBancaria;
		
		
		/**
		 * Datos para el Banco
		 */
		private Long idBanco;
		private String bancoNombre;
		private boolean estadoBanco;
		
		
		
		/**
		 * Datos para Sucursal
		 */
		private Long idSucursal;
		private String codigoSucursal;
		private String nombreSucursal;
		private String prefijoSucursal;
		
		/**
		 * Datos para Centro de Costos
		 */
		private Long idCentroCostos;
		private String codigoCentroCostos;
		private String nombreCentroCostos;
		private String prefijoCentroCostos;
		private boolean estadoCentroCostos;
		
		/**
		 * Datos para Area
		 */
		private Long idArea;
		private String nombreArea;
		private boolean estadoArea;
		
		/**
		 * Datos para el Jefe
		 */
		private Long idJefe;
		private Enum tipoEmpleado;
		private String idpJefe;
		private String nombreJefe;
	}
	
	@Data
	public static class Empleado{
		private List<ProfesionDto.Response> profesion;
		private List<CargoDto.Response> cargo;
		private List<AreaDto.Response> area;
		private List<SucursalDto.Response> sucursal;
		private List<CentroCostosDto.Response> centrocostos;
		private List<BancoDto.Response> banco;
		private List<PersonaDto.Response> jefe;
	}
	
}
