package com.saims.sgrf.dto;

import lombok.Data;

@Data
public class LoginDtoResponse {
	private String user;
	private String nombre;
	private String email;
	private String token;
	private String refresh;
}
