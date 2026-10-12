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

import com.saims.sgrf.dto.DatosDto;
import com.saims.sgrf.service.DatosService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sgrf/user/usuario/datos")
//@RequestMapping("/user/usuario/datos")
@RequiredArgsConstructor
public class DatosController {
	private final DatosService datosService;
	
	@PostMapping("/addDatos")
	public ResponseEntity<DatosDto.Response> addDatos(@RequestBody DatosDto.Request request){
		try {
			return new ResponseEntity<>(this.datosService.AddDatos(request),HttpStatus.CREATED);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/getEmpleadosFile")
	public ResponseEntity<List<DatosDto.Request>> getEmpleadosFile(){
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
	@GetMapping("/getDatos")
	public ResponseEntity<DatosDto.Response>getDatos(@RequestParam (value="idPersona") Long idPersona){
		try {
			return new ResponseEntity<>(this.datosService.getDatos(idPersona) ,HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/getDatosUsuario")
	public ResponseEntity<DatosDto.Response>getEstadoUsuario(@RequestParam (value="idUsuario") Long idUsuario){
		try {
			return new ResponseEntity<>(this.datosService.getDatosUsuario(idUsuario) ,HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getDatosEmpleados")
	public ResponseEntity<DatosDto.Empleado>getDatosEmpleados(){
		try {
			return new ResponseEntity<>(this.datosService.getDatosEmpleados() ,HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getPerfil")
	public ResponseEntity<DatosDto.Perfil>getPerfil(@RequestParam (value="idPersona") Long idPersona){
		try {
			return new ResponseEntity<>(this.datosService.getPerfil(idPersona) ,HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
