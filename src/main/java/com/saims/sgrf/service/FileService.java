package com.saims.sgrf.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	String createProfile(MultipartFile file)throws Exception;

}
