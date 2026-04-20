package com.saims.sgrf.dto;

import lombok.Data;

@Data
public class EmpleadoDtoResponse {
	private Long id;
	private Long idCargo;
	private Long idPersona;
	private Long idProfesion;
	private Long idSucursal;
	private String tipo;
	private String idpJefe;
	private boolean estado;
	private String fechaCreacion;
	private String fechaActualizacion;
}
