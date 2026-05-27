package com.saims.sgrf.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saims.sgrf.dto.UsuarioDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.enums.Rol;
import com.saims.sgrf.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sgrf/user/usuario")
@RequiredArgsConstructor
public class UsuarioController {
	private final UsuarioService usuarioService;
	
	@PutMapping("/updateUsuarioPassword")
	public boolean updateUsuarioPassword(@RequestBody UsuarioDto.Password password){
		try {
			return this.usuarioService.updateUsuarioPassword(password);
		}catch (Exception e) {
			return false;
		}
	}
	
	@PutMapping("/updateUsuarioRol")
	public boolean updateUsuarioRol(@RequestBody UsuarioDto.UserRol rol){
		try {
			return this.usuarioService.updateUsuarioRol(rol);
		}catch (Exception e) {
			return false;
		}
	}
	
	@PutMapping("/updateUsuarioImagen")
	public ResponseEntity<UsuarioDto.Response> updateUsuarioImagen(@RequestBody UsuarioDto.Response response){
		try {
			return new ResponseEntity<>(this.usuarioService.updateUsuarioImagen(response),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/updatePassword")
	public boolean updatePassword(@RequestBody UsuarioDto.Password password){
		try {
			return this.usuarioService.updatePassword(password);
		}catch (Exception e) {
			return false;
		}
	}
	
	@GetMapping("/findAllResponse")
	public ResponseEntity<List<UsuarioDto.Response>> findAllResponse(@RequestParam (value = "estado") Estado estado){
		try {
			return new ResponseEntity<>(this.usuarioService.findAllResponse(estado),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/findByRol")
	public ResponseEntity<List<UsuarioDto.Response>> findByRol(@RequestParam (value = "rol") Rol rol){
		try {
			return new ResponseEntity<>(this.usuarioService.findByRol(rol),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getUsuarioValido")
	public ResponseEntity<UsuarioDto.Response> getUsuarioValido(@RequestParam (value = "usuario") String usuario, @RequestParam (value = "email") String email){
		try {
			return new ResponseEntity<>(this.usuarioService.getUsuarioValido(usuario,email),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/updateUsuarioPernicions")
	public ResponseEntity<Void> updateUsuarioPernicions(@RequestBody UsuarioDto.Response response){
		try {
			this.usuarioService.updateUsuarioPernicions(response);
	        return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}
