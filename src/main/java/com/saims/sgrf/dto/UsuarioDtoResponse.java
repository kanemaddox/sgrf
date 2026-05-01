package com.saims.sgrf.dto;

import lombok.Data;

@Data
public class UsuarioDtoResponse {
	private Long id;
	private String usuario;
	private String password;
	private Boolean estado;
	private String imagen;
	private String fechaCreacion;
	private String fechaActualizacion;
}
