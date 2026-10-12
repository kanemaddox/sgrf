package com.saims.sgrf.dto;

import java.util.Map;

import com.saims.sgrf.enums.Rol;

import lombok.Data;

public class LoginDto {
	
	@Data
	public static class Request{
		private String user;
		private String pass;	
	}
	
	@Data
	public static class Response{
		private Long idUsuario;
		private String user;
		private String nombre;
		private String email;
		private String imagen;
		private PersonaDto.Response persona;
		private Map<String, Object> pernicions;
		private String idpName;
		private Rol rol;
		private int intentos;
		private boolean pass; 
		private String token;
		private String refresh;
		private Long expiresIn;
		private Long expiresAt;
	}
	
}
