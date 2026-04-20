package com.saims.sgrf.dto;

import lombok.Data;

@Data
public class CuentaBancariaDtoResponse {
	private Long id;
	private String numero;
	private String tipoCuenta;
	private Boolean estado;
	private Long idPersona;
	private Long idBanco;
	private String fechaCreacion;
	private String fechaActualizacion;
}
