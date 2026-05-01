package com.saims.sgrf.dto;

import lombok.Data;

@Data
public class LoginDtoResponse {
	private Long idUsuario;
	private String user;
	private String nombre;
	private String email;
	private String imagen;
	private PersonaDtoResponse persona;
	private String token;
	private String refresh;
}
