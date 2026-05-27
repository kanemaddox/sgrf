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

import com.saims.sgrf.dto.BancoDto;
import com.saims.sgrf.service.BancoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/erpm/banco")
@RequiredArgsConstructor
public class BancoController {
	
	private final BancoService bancoService;
	
	@PostMapping("/create")
	public ResponseEntity<BancoDto.Response> create(@RequestBody BancoDto.Request request){
		try {
			return new ResponseEntity<>(this.bancoService.createBanco(request) ,HttpStatus.CREATED);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getLista")
	public ResponseEntity<List<BancoDto.Response>> getLista(){
		try {
			return new ResponseEntity<>(this.bancoService.getAll() ,HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<BancoDto.Response>update(@RequestBody BancoDto.Response response){
		try {
			return new ResponseEntity<>(this.bancoService.update(response),HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);			
		}
	}

}
