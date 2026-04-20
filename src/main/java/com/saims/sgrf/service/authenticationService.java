package com.saims.sgrf.service;

import com.saims.sgrf.dto.LoginDtoRequest;
import com.saims.sgrf.dto.LoginDtoResponse;

public interface authenticationService {
	LoginDtoResponse login(LoginDtoRequest request);
}
