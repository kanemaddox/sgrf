package com.saims.sgrf.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saims.sgrf.dto.EmpleadoDtoRequest;
import com.saims.sgrf.dto.EmpleadoDtoResponse;
import com.saims.sgrf.service.EmpleadoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/erpm/empleado")
@RequiredArgsConstructor
public class EmpleadoController {
	private final EmpleadoService empleadoService;
	
	@PostMapping("/create")
	public ResponseEntity<EmpleadoDtoResponse>create(@RequestBody EmpleadoDtoRequest request){
		try {
			return new ResponseEntity<>(this.empleadoService.create(request) ,HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
