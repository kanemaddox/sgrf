package com.saims.sgrf.service;

import java.util.List;

import com.saims.sgrf.dto.RdgDevolverDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.RdgDevolverModel;
import com.saims.sgrf.model.RdgModel;

public interface RdgDevolverService {
	
	RdgDevolverModel createOrGet(RdgDevolverDto.Request request, RdgModel rdg);
	List<RdgDevolverDto.Response> findByIdRdgByAgregadoResponse(Long idRdg,Estado agregado);

}
