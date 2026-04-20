package com.saims.sgrf.dto;

import lombok.Data;

@Data

public class DatosDtoRequest {
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
	
	

}
