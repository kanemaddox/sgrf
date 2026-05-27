package com.saims.sgrf.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saims.sgrf.dto.LoginDto;
import com.saims.sgrf.service.authenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping ("/sgrf")
@RequiredArgsConstructor

public class LoginController {
	private final authenticationService authenticationService;
	
	@PostMapping ("/login")
	public ResponseEntity<LoginDto.Response>login(@RequestBody LoginDto.Request request){
		try {
			return new ResponseEntity<>(this.authenticationService.login(request),HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}