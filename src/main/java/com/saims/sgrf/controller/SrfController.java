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

import com.saims.sgrf.dto.SrfDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.service.SrfService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sgrf/user/srf")
//@RequestMapping("/user/srf")
@RequiredArgsConstructor
public class SrfController {
	
	private final SrfService srfService;
	
	@PostMapping("/createSrf")
	public ResponseEntity<SrfDto.Response> createSrf(@RequestBody SrfDto.Request request){
		try {
			return new ResponseEntity<>(this.srfService.createSrf(request) ,HttpStatus.CREATED);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/getSrfUsuarioEstado")
	public ResponseEntity<List<SrfDto.Stado>> getSrfUsuarioEstado(@RequestParam (value="idPersona") Long idPersona, @RequestParam (value="estado") Estado estado){
		try {
			return new ResponseEntity<>(this.srfService.getSrfUsuarioEstado(idPersona, estado),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getSrfUsuarioGestion")
	public ResponseEntity<List<SrfDto.Stado>> getSrfUsuarioGestion(@RequestParam (value="idUsuario") Long idPersona, @RequestParam (value="gestion") int gestion){
		try {
			return new ResponseEntity<>(this.srfService.getSrfUsuarioGestion(idPersona, gestion),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getSrf")
	public ResponseEntity<SrfDto.Detalle> getSrf(@RequestParam (value="idSrf") Long idSrf){
		try {
			return new ResponseEntity<>(this.srfService.getSrf(idSrf),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getSrfUsuario")
	public ResponseEntity<SrfDto.Detalle> getSrfUsuario(@RequestParam (value="idSrf") String idSrf,@RequestParam (value="idPersona") Long idPersona){
		try {
			return new ResponseEntity<>(this.srfService.getSrfUsuario(idSrf, idPersona),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/getSrfAdmin")
	public ResponseEntity<SrfDto.Detalle> getSrfAdmin(@RequestParam (value="idUsuario") Long idUsuario,@RequestParam (value="idSrf") String idSrf){
		try {
			return new ResponseEntity<>(this.srfService.getSrfAdmin(idUsuario,idSrf),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/updateSrf")
	public ResponseEntity<SrfDto.Response> updateSrf(@RequestBody SrfDto.Response response){
		try {
			return new ResponseEntity<>(this.srfService.updateSrf(response),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getSrfEstado")
	public ResponseEntity<List<Integer>> getSrfEstado(@RequestParam (value = "idPersona") Long idPersona){
		try {
			return new ResponseEntity<>(this.srfService.getSrfEstado(idPersona),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
