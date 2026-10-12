package com.saims.sgrf.service;

import java.util.List;

import com.saims.sgrf.dto.CuentaDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.CuentaModel;

public interface CuentaService {
	CuentaDto.Response createOrGet(CuentaDto.Request request);
	List<CuentaDto.Response> getCuentas(Estado categoria);
	CuentaDto.Response updateCuenta(CuentaDto.Response response);
	List<CuentaModel> findAll();

}
