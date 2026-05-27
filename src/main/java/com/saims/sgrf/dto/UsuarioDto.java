package com.saims.sgrf.dto;

import java.util.Map;

import com.saims.sgrf.enums.Rol;
import com.saims.sgrf.model.PersonaModel;

import lombok.Data;


public class UsuarioDto {

	@Data
	public static class Request{
		private String usuario;
		private String password;
		private Boolean estado;
	}
	
	@Data
	public static class Response{
		private Long id;
		private String usuario;
		private String password;
		private Boolean estado;
		private String imagen;
		private PersonaModel persona;
		private String fechaCreacion;
		private String fechaActualizacion;
		private Map<String, Object> pernicions;
	}
	
	@Data
	public static class UserRol{
		private Long idAdmin;
		private String pass;
		private Long idUser;
		private Rol rol;
	}
	
	@Data
	public static class Password{
		private Long id;
		private String pass1;
		private String pass2;
		private String pass3;
	}
	
}
