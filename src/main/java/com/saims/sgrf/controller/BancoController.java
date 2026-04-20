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

import com.saims.sgrf.dto.BancoDtoRequest;
import com.saims.sgrf.dto.BancoDtoResponse;
import com.saims.sgrf.service.BancoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/erpm/banco")
@RequiredArgsConstructor
public class BancoController {
	
	private final BancoService bancoService;
	
	@PostMapping("/create")
	public ResponseEntity<BancoDtoResponse> create(@RequestBody BancoDtoRequest request){
		try {
			return new ResponseEntity<>(this.bancoService.createBanco(request) ,HttpStatus.CREATED);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getLista")
	public ResponseEntity<List<BancoDtoResponse>> getLista(){
		try {
			return new ResponseEntity<>(this.bancoService.getAll() ,HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<BancoDtoResponse>update(@RequestBody BancoDtoResponse response){
		try {
			return new ResponseEntity<>(this.bancoService.update(response),HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);			
		}
	}

}
