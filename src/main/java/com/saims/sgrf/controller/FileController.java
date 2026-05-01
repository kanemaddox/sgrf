package com.saims.sgrf.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.saims.sgrf.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sgrf/user/upload")
@RequiredArgsConstructor
public class FileController {
	private final FileService fileService;
	
	@PostMapping("/createProfile")
	public ResponseEntity<String> createProfile(@RequestParam(value = "file") MultipartFile file){
		try {
			return new ResponseEntity<>(this.fileService.createProfile(file) ,HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
