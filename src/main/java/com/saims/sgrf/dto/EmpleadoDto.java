package com.saims.sgrf.dto;

import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.enums.TipoEmpleado;

import lombok.Data;

public class EmpleadoDto {
	
	@Data
	public static class Request{
		private Long idCargo;
		private Long idPersona;
		private Long idProfesion;
		private Long idSucursal;
		private TipoEmpleado tipoEmpleado;
		private String idpJefe;
		private Boolean estado;	
	}
	
	@Data
	public static class Response{
		private Long id;
		private String tipo;
		private Boolean estado;
		private String fechaCreacion;
		private String fechaActualizacion;
		private PersonaDto.Response persona;
	}
	
	@Data
	public static class Cargo{
		private Long id;	
		private String cargo;
		private boolean estado;
	    private String idpJefe;
	    private String jefe;
		private String fechaCreacion;
		private String fechaActualizacion;
	}
	
	@Data
	public static class Persona{
		private Long id;
		private TipoEmpleado tipo;
		private Estado estado;
		private PersonaDto.Pendiente persona;
	}
}
