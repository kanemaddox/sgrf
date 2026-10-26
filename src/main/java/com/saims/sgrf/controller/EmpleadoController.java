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

import com.saims.sgrf.dto.EmpleadoDto;
import com.saims.sgrf.dto.PersonaDto;
import com.saims.sgrf.enums.TipoEmpleado;
import com.saims.sgrf.service.EmpleadoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sgrf/user/empleado")
//@RequestMapping("/user/empleado")
@RequiredArgsConstructor
public class EmpleadoController {
	private final EmpleadoService empleadoService;

	@PostMapping("/create")
	public ResponseEntity<EmpleadoDto.Response> create(@RequestBody EmpleadoDto.Request request) {
		try {
			return new ResponseEntity<>(this.empleadoService.create(request), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getPersona")
	public ResponseEntity<EmpleadoDto.Response> findByPersonaId(@RequestParam(value = "idPersona") Long idPersona) {
		try {
			return new ResponseEntity<>(this.empleadoService.findByPersonaId(idPersona), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/updateCorreo")
	public ResponseEntity<EmpleadoDto.Response> updateCorreo(@RequestBody PersonaDto.Response response){
		try {
			return new ResponseEntity<>(this.empleadoService.updateCorreo(response),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/findByTipo")
	public ResponseEntity<List<EmpleadoDto.Response>> findByTipo(@RequestParam(value = "tipo") TipoEmpleado tipo) {
		try {
			return new ResponseEntity<>(this.empleadoService.findByTipoResponse(tipo), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
