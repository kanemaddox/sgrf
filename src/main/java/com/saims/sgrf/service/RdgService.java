package com.saims.sgrf.service;

import java.util.List;

import com.saims.sgrf.dto.RdgDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.EmpleadoModel;
import com.saims.sgrf.model.RdgAprobacionModel;
import com.saims.sgrf.model.RdgModel;
import com.saims.sgrf.model.SrfModel;

public interface RdgService {
	
	RdgModel createTramite(SrfModel srf, EmpleadoModel empleado);
	RdgModel createOrGet(SrfModel srf, EmpleadoModel empleado);
	List<RdgDto.Srf> getRdgUsuarioEstado(Long idPersona, Estado estado);
	RdgDto.Response getRdg(Long idRdg);
	RdgDto.CreateRdg getRdgUsuario(String idRdg, Long idPersona);
	RdgModel createUpdateRdg(RdgDto.Response response);
	RdgModel createRdg(RdgDto.Request request);
	List<RdgDto.Lista> getRdgLista(Long idPersona, Estado estado);
	RdgDto.Detalle getRdgDetalle(String idRdg, Long idPersona);
	RdgDto.Solicitud updateRdgSolicitud(RdgDto.Solicitud response);
	List<Integer> getRdgEstado(Long idPersona);
	RdgModel saveRdg(RdgDto.Response response);
	RdgDto.Publico getRdgPublico (String idRdg);
	List<RdgDto.Lista> getRdgUsuarioGestion(Long idUsuario, int gestion);
	RdgDto.Detalle getRdgAdmin(Long idUsuario, String idRdg);
}
