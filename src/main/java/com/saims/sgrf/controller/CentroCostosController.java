package com.saims.sgrf.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saims.sgrf.dto.CentroCostosDto;
import com.saims.sgrf.service.CentroCostosService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sgrf/user/centrocostos")
@RequiredArgsConstructor
public class CentroCostosController {
	
	private final CentroCostosService centroCostosService;
	
	@GetMapping("/findAll")
	public ResponseEntity<List<CentroCostosDto.Response>> findAll() {
		try {
			return new ResponseEntity<>(this.centroCostosService.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
