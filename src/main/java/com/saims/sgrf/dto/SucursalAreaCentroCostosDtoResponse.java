package com.saims.sgrf.dto;

import lombok.Data;

@Data
public class SucursalAreaCentroCostosDtoResponse {
	private Long id;
	private Long idArea;
	private Long idCentroCostos;
	private Long idSucursal;
	private String fechaCreacion;
	private String fechaActualizacion;
}
