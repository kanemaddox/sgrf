package com.saims.sgrf.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	String createProfile(MultipartFile file)throws Exception;
	String createDocument(MultipartFile file) throws IOException;

}
