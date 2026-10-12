package com.saims.sgrf.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saims.sgrf.dto.LoginDto;
import com.saims.sgrf.dto.RdgDetalleDto;
import com.saims.sgrf.dto.RdgDto;
import com.saims.sgrf.dto.SrfDto;
import com.saims.sgrf.service.RdgDetalleService;
import com.saims.sgrf.service.RdgService;
import com.saims.sgrf.service.SrfService;
import com.saims.sgrf.service.authenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping ("/sgrf/publico")
//@RequestMapping("/publico")
@RequiredArgsConstructor

public class LoginController {
	private final authenticationService authenticationService;
	private final SrfService srfService;
	private final RdgService rdgService;
	private final RdgDetalleService detalleService;

	@PostMapping("/login")
	public ResponseEntity<LoginDto.Response> login(@RequestBody LoginDto.Request request) {
		try {
			return new ResponseEntity<>(this.authenticationService.login(request), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getSrfPublico")
	public ResponseEntity<SrfDto.Publico> getSrfPublico(@RequestParam(value = "idSrf") String idSrf) {
		try {
			SrfDto.Publico srf = this.srfService.getSrfPublico(idSrf);
			if (srf.getId().longValue() == 0L) {
				return ResponseEntity.notFound().build();
			}
			return new ResponseEntity<>(srf, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getRdgPublico")
	public ResponseEntity<RdgDto.Publico> getRdgPublico(@RequestParam(value = "idRdg") String idRdg) {
		try {
			RdgDto.Publico rdg = this.rdgService.getRdgPublico(idRdg);
			if (rdg.getId().longValue() == 0L) {
				return ResponseEntity.notFound().build();
			}
			return new ResponseEntity<>(rdg, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getDocumentoPublico")
	public ResponseEntity<RdgDetalleDto.Publico> getDocumentoPublico(@RequestParam(value = "idDetalle") String idDetalle) {
		try {
			RdgDetalleDto.Publico detalle = this.detalleService.getDocumentoPublico(idDetalle);
			if (detalle.getIdDetalle().longValue() == 0L) {
				return ResponseEntity.notFound().build();
			}
			return new ResponseEntity<>(detalle, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}