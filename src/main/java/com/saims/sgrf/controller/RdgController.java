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

import com.saims.sgrf.dto.RdgDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.RdgModel;
import com.saims.sgrf.service.RdgService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sgrf/user/rdg")
//@RequestMapping("/user/rdg")
@RequiredArgsConstructor
public class RdgController {
	private final RdgService rdgService;
	
	@GetMapping("/getRdgUsuarioEstado")
	public ResponseEntity<List<RdgDto.Srf>> getRdgUsuarioEstado(@RequestParam (value="idPersona") Long idPersona, @RequestParam (value="estado") Estado estado){
		try {
			return new ResponseEntity<>(this.rdgService.getRdgUsuarioEstado(idPersona, estado),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getRdgEstado")
	public ResponseEntity<List<Integer>> getRdgEstado(@RequestParam (value="idPersona") Long idPersona){
		try {
			return new ResponseEntity<>(this.rdgService.getRdgEstado(idPersona),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getRdgLista")
	public ResponseEntity<List<RdgDto.Lista>> getRdgLista(@RequestParam (value="idPersona") Long idPersona, @RequestParam (value="estado") Estado estado){
		try {
			return new ResponseEntity<>(this.rdgService.getRdgLista(idPersona, estado),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getRdg")
	public ResponseEntity<RdgDto.Response> getRdg(@RequestParam (value="idRdg") Long idRdg){
		try {
			return new ResponseEntity<>(this.rdgService.getRdg(idRdg),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getRdgUsuario")
	public ResponseEntity<RdgDto.CreateRdg> getRdgUsuario(@RequestParam (value="idRdg") String idRdg,@RequestParam (value="idPersona") Long idPersona){
		try {
			return new ResponseEntity<>(this.rdgService.getRdgUsuario(idRdg,idPersona),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getRdgDetalle")
	public ResponseEntity<RdgDto.Detalle> getRdgDetalle(@RequestParam (value="idRdg") String idRdg,@RequestParam (value="idPersona") Long idPersona){
		try {
			return new ResponseEntity<>(this.rdgService.getRdgDetalle(idRdg,idPersona),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/createRdg")//Sin formulario de Solicitud
	public ResponseEntity<RdgModel> createRdg(@RequestBody RdgDto.Request request){
		try {
			return new ResponseEntity<>(this.rdgService.createRdg(request),HttpStatus.CREATED);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/createUpdateRdg")// Con Formulario de Solicitud
	public ResponseEntity<RdgModel> createUpdateRdg(@RequestBody RdgDto.Response response){
		try {
			return new ResponseEntity<>(this.rdgService.createUpdateRdg(response),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PutMapping("/saveRdg") 
	public ResponseEntity<RdgModel> save(@RequestBody RdgDto.Response response){
		try {
			return new ResponseEntity<>(this.rdgService.saveRdg(response),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/updateRdgSolicitud")
	public ResponseEntity<RdgDto.Solicitud> updateRdgSolicitud(@RequestBody RdgDto.Solicitud response){
		try {
			return new ResponseEntity<>(this.rdgService.updateRdgSolicitud(response),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}
