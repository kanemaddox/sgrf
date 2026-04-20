package com.saims.sgrf.dto;

import lombok.Data;

@Data
public class BancoDtoResponse {
	private Long id;
	private String nombre;
	private Boolean estado;
	private String fechaCreacion;
	private String fechaActualizacion;
}
