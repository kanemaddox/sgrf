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
import com.saims.sgrf.dto.RdgDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.service.AprobacionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sgrf/user/aprobacion")
//@RequestMapping("/user/aprobacion")
@RequiredArgsConstructor
public class AprobacionController {
	private final AprobacionService.Srf aprobacionService;
	private final AprobacionService.Rdg rdgService;
	
	
	@GetMapping("/getSrfEstado")
	public ResponseEntity<List<AprobacionDto.Stado>> getSrfEstado(@RequestParam (value="idPersona") Long idPersona,@RequestParam (value="estado") Estado estado){
		try {
			return new ResponseEntity<>(this.aprobacionService.getSrfEstado(idPersona,estado),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getApr")
	public ResponseEntity<AprobacionDto.Revisar> getSrf(@RequestParam (value="idPersona") Long idPersona,@RequestParam (value="idApr") String idApr){
		try {
			return new ResponseEntity<>(this.aprobacionService.getApr(idPersona,idApr),HttpStatus.OK);
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
	
	/**=====================================================================================================
	 * para Arpobaciones de RDG*/
	
	@GetMapping("/rdg/getEstadoRdg")
	public ResponseEntity<List<RdgDto.ListaRevisar>> getEstadoRdg(@RequestParam (value="idPersona") Long idPersona,@RequestParam (value="estado") Estado estado){
		try {
			return new ResponseEntity<>(this.rdgService.getEstadoRdg(idPersona,estado),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/rdg/getRdgRevisar")
	public ResponseEntity<RdgDto.Revisar> getRdgRevisar(@RequestParam (value="idPersona") Long idPersona,@RequestParam (value="idRdg") String idRdg){
		try {
			return new ResponseEntity<>(this.rdgService.getRdgRevisar(idPersona, idRdg),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/rdg/updateAprobacion")
	public ResponseEntity<RdgDto.Revisar> updateAprobacionRdg(@RequestBody AprobacionDto.AprobacionRdg response){
		try {
			return new ResponseEntity<>(this.rdgService.updateAprobacion(response),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
