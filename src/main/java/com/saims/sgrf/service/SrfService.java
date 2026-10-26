package com.saims.sgrf.service;

import java.util.List;

import com.saims.sgrf.dto.SrfDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.SrfModel;

public interface SrfService {
	
	SrfDto.Response createSrf(SrfDto.Request request);
	List<SrfDto.Stado> getSrfUsuarioEstado(Long idPersona, Estado estado);
	SrfDto.Detalle getSrf(Long idSrf);
	SrfDto.Response updateSrf(SrfDto.Response response);
	boolean updateEstado(SrfModel request);
	SrfDto.Detalle getSrfUsuario(String idSrf,Long idPersona);
	SrfModel findById(Long idSrf);
	List<Integer> getSrfEstado(Long idPersona);
	SrfDto.Publico getSrfPublico(String token);
	List<SrfDto.Stado> getSrfUsuarioGestion(Long idUsuario, int gestion);
	SrfDto.Detalle getSrfAdmin(Long idUsuario,String idSrf);

}
