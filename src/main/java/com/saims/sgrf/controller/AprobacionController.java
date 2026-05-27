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

import com.saims.sgrf.dto.AprobacionDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.service.AprobacionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sgrf/user/aprobacion")
@RequiredArgsConstructor
public class AprobacionController {
	private final AprobacionService aprobacionService;
	
	
	@GetMapping("/getPendientes")
	public ResponseEntity<List<AprobacionDto.Pendiente>> getPendientes(@RequestParam (value="idUsuario") Long idUsuario,@RequestParam (value="estado") Estado estado){
		try {
			return new ResponseEntity<>(this.aprobacionService.getPendientes(idUsuario,estado),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getApr")
	public ResponseEntity<AprobacionDto.Revisar> getSrf(@RequestParam (value="idUsuario") Long idUsuario,@RequestParam (value="idApr") Long idApr){
		try {
			return new ResponseEntity<>(this.aprobacionService.getApr(idUsuario,idApr),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/updateAprobacion")
	public ResponseEntity<AprobacionDto.Response> updateAprobacion(@RequestBody AprobacionDto.UpdateRequest request){
		try {
			return new ResponseEntity<>(this.aprobacionService.updateAprobacion(request),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
