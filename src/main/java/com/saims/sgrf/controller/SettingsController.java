package com.saims.sgrf.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saims.sgrf.dto.SettingsDto;
import com.saims.sgrf.service.SettingsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sgrf/user/setting")
@RequiredArgsConstructor
public class SettingsController {
	private final SettingsService settingsService; 
	
	@PutMapping("/updateSetting")
	public ResponseEntity<SettingsDto.Response> updateFormulario(@RequestBody SettingsDto.Response response){
		try {
			return new ResponseEntity<>(this.settingsService.updateSettings(response),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getSetting")
	public ResponseEntity<SettingsDto.Response> getFormulario(@RequestParam (value="idSett") Long idSett){
		try {
			return new ResponseEntity<>(this.settingsService.getValue(idSett),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
