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

import com.saims.sgrf.dto.AreaDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.service.AreaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sgrf/user/area")
//@RequestMapping("/user/area")
@RequiredArgsConstructor
public class AreaController {
	
	private final AreaService areaService;
	
	@PostMapping("/create")
	public ResponseEntity<AreaDto.Response> create(@RequestBody AreaDto.Request request){
		try {
			return new ResponseEntity<>(this.areaService.createArea(request) ,HttpStatus.CREATED);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/createDatos")
	public ResponseEntity<AreaDto.Response> createDatos(@RequestBody AreaDto.Request request){
		try {
			return new ResponseEntity<>(this.areaService.createDatos(request) ,HttpStatus.CREATED);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/findByAll")
	public ResponseEntity<List<AreaDto.Response>> findByAll(@RequestParam (value="estado") Estado estado){
		try {
			return new ResponseEntity<>(this.areaService.findByAll(estado),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getEstructura")
	public ResponseEntity<List<AreaDto.Estructura>> getEstructura(){
		try {
			return new ResponseEntity<>(this.areaService.getEstructura(),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<AreaDto.Response> update(@RequestBody AreaDto.Response response){
		try {
			return new ResponseEntity<>(this.areaService.update(response),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
