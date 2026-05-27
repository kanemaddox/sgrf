package com.saims.sgrf.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saims.sgrf.dto.SucursalDto;
import com.saims.sgrf.service.SucursalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sgrf/user/sucursal")
@RequiredArgsConstructor
public class SucursalController {
	private final SucursalService sucursalService;

	@GetMapping("/findAll")
	public ResponseEntity<List<SucursalDto.Response>> findAll() {
		try {
			return new ResponseEntity<>(this.sucursalService.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
