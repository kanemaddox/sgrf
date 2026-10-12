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
		private String foto;
		
		/**
		 * Datos Para profesion  
		 */
		private String profesionNombre;
		
		/**
		 * Datos para cargo
		 */
		private String cargoNombre;
		private String emailEmpleado;
		
		/**
		 * Datos para el Banco y para Cuenta Bancaria
		 */
		private String bancoNombre;
		private String numero;
		private String tipoCuenta;
		
		/**
		 * Datos para Centro de Costos
		 */
		private String codigoCentroCostos;
		private String nombreCentroCostos;
		private String codigoSubCentroCostos;
		private String nombreSubCentroCostos;
		
		/**
		 * Datos para el Jefe
		 */
		private String idpJefe;
		private Boolean tipo;
		
		/**
		 * Datos para Contavilidad
		 */
		private String idpPago;
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
		private String emailEmpleado;
		private boolean estadoCargo;
		private Enum tipoEmpleado;
		
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
		 * Datos Area de Trabajo
		 */
		private Long idArea;
		private String codigoArea;
		private String nombreArea;
		private String prefijoArea;
		
		/**
		 * Datos para el Jefe
		 */
		private Long idJefe;
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
	
	@Data
	public static class Perfil{
		
		private Long idPersona;
		
		private String idp;
		private String nombre;
		private String email;
		private String celular;
		
		/**
		 * datos para archivo 
		 * */
		
		private String profesion;
		private String cargo;
		private String corporativo;
		
		private String centroCostos;
		private String subCentroCostos;
		private String codigoSubCentro;
		
		/**
		 * datos del Jefe*/
		
		private String idpJefe;
		private String jefe;
		
	}
}
