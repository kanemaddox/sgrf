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

import com.saims.sgrf.dto.CuentaDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.service.CuentaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sgrf/user/cuenta")
//@RequestMapping("/user/cuenta")
@RequiredArgsConstructor
public class CuentaController {
	
	private final CuentaService cuentaService;
	
	@PostMapping("/create")
	public ResponseEntity<CuentaDto.Response>create(@RequestBody CuentaDto.Request request){
		try {
			return new ResponseEntity<>(this.cuentaService.createOrGet(request) ,HttpStatus.CREATED);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getCuentas")
	public ResponseEntity<List<CuentaDto.Response>>getCuentas(@RequestParam (value="categoria") Estado categoria){
		try {
			return new ResponseEntity<>(this.cuentaService.getCuentas(categoria),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/updateCuenta")
	public ResponseEntity<CuentaDto.Response>updateCuenta(@RequestBody CuentaDto.Response response){
		try {
			return new ResponseEntity<>(this.cuentaService.updateCuenta(response),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
