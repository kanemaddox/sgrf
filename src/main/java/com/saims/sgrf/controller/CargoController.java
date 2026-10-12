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

import com.saims.sgrf.dto.CargoDto;
import com.saims.sgrf.service.CargoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sgrf/user/cargo")
//@RequestMapping("/user/cargo")
@RequiredArgsConstructor
public class CargoController {
	
	private final CargoService cargoService;
	
	@PostMapping("/create")
	public ResponseEntity<CargoDto.Response>create(@RequestBody CargoDto.Request request){
		try {
			return new ResponseEntity<>(this.cargoService.createCargo(request),HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getLista")
	public ResponseEntity<List<CargoDto.Response>>getLista(){
		try {
			return new ResponseEntity<>(this.cargoService.getAll(),HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<CargoDto.Response>update(@RequestBody CargoDto.Response response){
		try {
			return new ResponseEntity<>(this.cargoService.update(response),HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
