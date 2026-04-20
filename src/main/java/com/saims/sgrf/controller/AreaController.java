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

import com.saims.sgrf.dto.AreaDtoRequest;
import com.saims.sgrf.dto.AreaDtoResponse;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.service.AreaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/erpm/area")
@RequiredArgsConstructor
public class AreaController {
	
	private final AreaService areaService;
	
	@PostMapping("/create")
	public ResponseEntity<AreaDtoResponse> create(@RequestBody AreaDtoRequest request){
		try {
			return new ResponseEntity<>(this.areaService.createArea(request) ,HttpStatus.CREATED);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getLista")
	public ResponseEntity<List<AreaDtoResponse>> getLista(@RequestParam (value="estado") Estado estado){
		try {
			return new ResponseEntity<>(this.areaService.findByAll(estado),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<AreaDtoResponse> update(@RequestBody AreaDtoResponse response){
		try {
			return new ResponseEntity<>(this.areaService.update(response),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
