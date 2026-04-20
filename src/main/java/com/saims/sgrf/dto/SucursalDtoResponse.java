package com.saims.sgrf.dto;

import lombok.Data;

@Data
public class SucursalDtoResponse {
	private Long id;
	private String nombre;
	private String codigo;
	private String prefijo;
	private String fechaCreacion;
	private String fechaActualizacion;
}
