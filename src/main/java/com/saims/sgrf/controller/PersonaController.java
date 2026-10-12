package com.saims.sgrf.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saims.sgrf.dto.PersonaDto;
import com.saims.sgrf.service.PersonaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sgrf/user/persona")
//@RequestMapping("/user/persona")
@RequiredArgsConstructor
public class PersonaController {
	private final PersonaService personaService;
	
	@PutMapping("/updateCelular")
	public ResponseEntity<PersonaDto.Response> updateCelular(@RequestBody PersonaDto.Response response){
		try {
			return new ResponseEntity<>(this.personaService.updateCelular(response),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/updateFull")
	public ResponseEntity<PersonaDto.Response> updateFull(@RequestBody PersonaDto.Response response){
		try {
			return new ResponseEntity<>(this.personaService.updateFull(response),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
