package com.saims.sgrf.dto;

import lombok.Data;

public class DepartamentoDto {
	
	@Data
	public static class Request{
		private String nombre;
	}
	
	@Data
	public static class Response{
		private Long id;
		private String departamento;
	}

}
