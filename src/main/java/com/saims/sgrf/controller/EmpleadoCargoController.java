package com.saims.sgrf.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saims.sgrf.dto.EmpleadoDto;
import com.saims.sgrf.service.EmpleadoCargoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sgrf/user/empleadocargo")
//@RequestMapping("/user/empleadocargo")
@RequiredArgsConstructor
public class EmpleadoCargoController {
	private final EmpleadoCargoService empleadoCargoService;
	
	@GetMapping("/getEmpleadoCargoUsuario")
	public ResponseEntity<List<EmpleadoDto.Cargo>>getEmpleadoCargoUsuario(@RequestParam (value="idUsuario") Long idUsuario){
		try {
			return new ResponseEntity<>(this.empleadoCargoService.getEmpleadoCargoUsuario(idUsuario),HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
