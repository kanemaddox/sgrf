package com.saims.sgrf.service;

import java.util.List;

import com.saims.sgrf.dto.DepartamentoDto;
import com.saims.sgrf.model.DepartamentoModel;

public interface DepartamentoService {
	
	DepartamentoModel createOrGet(String Nombre);
	List<DepartamentoDto.Response> findByAll();

}
