package com.saims.sgrf.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saims.sgrf.dto.DatosDtoRequest;
import com.saims.sgrf.dto.DatosDtoResponse;
import com.saims.sgrf.service.DatosService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/erpm/datos")
@RequiredArgsConstructor
public class DatosController {
	private final DatosService datosService;
	
	@PostMapping("/addDatos")
	public ResponseEntity<DatosDtoResponse> addDatos(@RequestBody DatosDtoRequest datosDtoRequest){
		try {
			return new ResponseEntity<>(this.datosService.AddDatos(datosDtoRequest),HttpStatus.CREATED);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/getEmpleadosFile")
	public ResponseEntity<List<DatosDtoResponse>> getEmpleadosFile(){
		try {
			return new ResponseEntity<>(this.datosService.getEmpleadosFile() ,HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/setEstado")
	public ResponseEntity<String>setEstado(@RequestParam (value="estado") boolean estado){
		try {
			this.datosService.setAllEstado(estado);
			return new ResponseEntity<>("Estado de empleados Actualizada" ,HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
