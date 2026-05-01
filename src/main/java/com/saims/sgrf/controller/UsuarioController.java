package com.saims.sgrf.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saims.sgrf.dto.UsuarioDtoResponse;
import com.saims.sgrf.dto.passwordDtoRequest;
import com.saims.sgrf.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sgrf/user/usuario")
@RequiredArgsConstructor
public class UsuarioController {
	private final UsuarioService usuarioService;
	
	@PutMapping("/updateUsuarioPassword")
	public boolean updateUsuarioPassword(@RequestBody passwordDtoRequest passwordDtoRequest){
		try {
			return this.usuarioService.updateUsuarioPassword(passwordDtoRequest);
		}catch (Exception e) {
			return false;
		}
	}
	@PutMapping("/updateUsuarioImagen")
	public ResponseEntity<UsuarioDtoResponse> updateUsuarioImagen(@RequestBody UsuarioDtoResponse response){
		try {
			return new ResponseEntity<>(this.usuarioService.updateUsuarioImagen(response),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/udatePassword")
	public ResponseEntity<UsuarioDtoResponse> updatePassword(@RequestParam (value = "idUsuario") Long idUsuario){
		try {
			return new ResponseEntity<>(this.usuarioService.updatePassword(idUsuario),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
