package com.saims.sgrf.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saims.sgrf.dto.CuentaBancariaDtoRequest;
import com.saims.sgrf.dto.CuentaBancariaDtoResponse;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.service.CuentaBancariaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/erpm/cuentabancaria")
@RequiredArgsConstructor
public class CuentaBancariaController {
	
	private final CuentaBancariaService cuentaBancariaService;
	
	@PostMapping("/create")
	public ResponseEntity<CuentaBancariaDtoResponse>create(@RequestBody CuentaBancariaDtoRequest request){
		try {
			return new ResponseEntity<>(this.cuentaBancariaService.createCuentaBancaria(request) ,HttpStatus.CREATED);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getLista")
	public ResponseEntity<List<CuentaBancariaDtoResponse>>getLista(@RequestParam (value="estado") Estado estado){
		try {
			return new ResponseEntity<>(this.cuentaBancariaService.findByAll(estado),HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<CuentaBancariaDtoResponse>update(@RequestBody CuentaBancariaDtoResponse response){
		try {
			return new ResponseEntity<>(this.cuentaBancariaService.update(response) ,HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
