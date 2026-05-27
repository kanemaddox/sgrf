package com.saims.sgrf.service;

import com.saims.sgrf.dto.LoginDto;

public interface authenticationService {
	LoginDto.Response login(LoginDto.Request request);
}
