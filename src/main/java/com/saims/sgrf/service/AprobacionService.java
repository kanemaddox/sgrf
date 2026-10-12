package com.saims.sgrf.service;

import java.util.List;

import com.saims.sgrf.dto.AprobacionDto;
import com.saims.sgrf.dto.RdgDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.RdgAprobacionModel;
import com.saims.sgrf.model.SrfAprobacionModel;

public interface AprobacionService {
	
	interface Srf{
		SrfAprobacionModel createModel(SrfAprobacionModel aprobacion);
		List<SrfAprobacionModel> findByIdSrf(Long idSrf);
		List<SrfAprobacionModel> getPersonaEstado(Long idPersona,Estado estado);
		List<AprobacionDto.Stado> getSrfEstado(Long idPersona, Estado estado);
		
		AprobacionDto.Revisar getApr(Long idPersona, String idApr);
		AprobacionDto.Response updateAprobacion(AprobacionDto.UpdateRequest request);
		
		List<SrfAprobacionModel> findByIdPersona(Long idPersona);
	}
	
	interface Rdg{
		RdgAprobacionModel createModel(RdgAprobacionModel aprobacion);
		List<AprobacionDto.RdgResponse> findByIdRdgResponse(Long idRdg);
		List<RdgDto.ListaRevisar> getEstadoRdg(Long idPersona,Estado estado);
		RdgDto.Revisar getRdgRevisar(Long idPersona,String idRdg);
		RdgDto.Revisar updateAprobacion(AprobacionDto.AprobacionRdg response);
		List<RdgAprobacionModel> findByIdPersona(Long idPersona);
		List<RdgAprobacionModel> findByIdRdgModel(Long idRdg);
		
	}
}
