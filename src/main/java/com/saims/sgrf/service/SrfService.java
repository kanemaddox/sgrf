package com.saims.sgrf.service;

import java.util.List;

import com.saims.sgrf.dto.SrfDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.SrfModel;

public interface SrfService {
	
	SrfDto.Response createSrf(SrfDto.Request request);
	List<SrfDto.Response> getSrfUsuarioEstado(Long idUsuario, Estado estado);
	SrfDto.Response getSrf(Long idSrf);
	SrfDto.Response updateSrf(SrfDto.Response response);
	boolean updateEstado(SrfModel request);

}
