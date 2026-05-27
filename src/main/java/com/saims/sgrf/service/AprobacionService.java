package com.saims.sgrf.service;

import java.util.List;

import com.saims.sgrf.dto.AprobacionDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.AprobacionModel;

public interface AprobacionService {
	
	AprobacionModel createModel(AprobacionModel aprobacion);
	List<AprobacionModel> findByIdSrf(Long idSrf);
	List<AprobacionModel> getPersonaEstado(Long idPersona,Estado estado);
	List<AprobacionDto.Pendiente> getPendientes(Long idUsuario, Estado estado);
	
	AprobacionDto.Revisar getApr(Long idUsuario, Long idApr);
	AprobacionDto.Response updateAprobacion(AprobacionDto.UpdateRequest request);
}
