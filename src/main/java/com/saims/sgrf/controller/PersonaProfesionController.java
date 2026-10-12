package com.saims.sgrf.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saims.sgrf.dto.PersonaDto;
import com.saims.sgrf.service.PersonaProfesionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sgrf/user/personaprofesion")
//@RequestMapping("/user/personaprofesion")
@RequiredArgsConstructor
public class PersonaProfesionController {
	private final PersonaProfesionService personaProfesionService;
	
	@GetMapping("/getPersonaProfesionUsuario")
	public ResponseEntity<List<PersonaDto.Profesion>>getPersonaProfesionUsuario(@RequestParam (value="idUsuario") Long idUsuario){
		try {
			return new ResponseEntity<>(this.personaProfesionService.getPersonaProfesionUsuario(idUsuario),HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
