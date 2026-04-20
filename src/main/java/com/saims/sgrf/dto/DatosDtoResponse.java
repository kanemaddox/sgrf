package com.saims.sgrf.dto;

import lombok.Data;

@Data
public class DatosDtoResponse {
	/**
	 * Datos para la Tabla persona
	 */
	private Long idPersona;
	private String idp;
	private String nombre;
	private String email;
	
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
