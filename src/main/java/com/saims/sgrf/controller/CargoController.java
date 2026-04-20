package com.saims.sgrf.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saims.sgrf.dto.CargoDtoRequest;
import com.saims.sgrf.dto.CargoDtoResponse;
import com.saims.sgrf.service.CargoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/erpm/cargo")
@RequiredArgsConstructor
public class CargoController {
	
	private final CargoService cargoService;
	
	@PostMapping("/create")
	public ResponseEntity<CargoDtoResponse>create(@RequestBody CargoDtoRequest request){
		try {
			return new ResponseEntity<>(this.cargoService.createCargo(request),HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getLista")
	public ResponseEntity<List<CargoDtoResponse>>getLista(){
		try {
			return new ResponseEntity<>(this.cargoService.getAll(),HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<CargoDtoResponse>update(@RequestBody CargoDtoResponse response){
		try {
			return new ResponseEntity<>(this.cargoService.update(response),HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
