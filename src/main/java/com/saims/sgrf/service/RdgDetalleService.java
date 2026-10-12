package com.saims.sgrf.service;

import java.util.List;
import java.util.Map;

import com.saims.sgrf.dto.RdgDetalleDto;
import com.saims.sgrf.dto.RdgDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.CuentaModel;
import com.saims.sgrf.model.RdgDetalleModel;
import com.saims.sgrf.model.RdgModel;

public interface RdgDetalleService {
	RdgDetalleModel createOrGet(RdgDetalleDto.Request request,CuentaModel cuenta, RdgModel rdg,Map<String, Object> mapa);
	List<RdgDetalleDto.Response> findByIdRdgResponse(Long idRdg);
	List<RdgDetalleModel> findByIdRdgModel(Long idRdg);
	RdgDetalleModel updateAprobado(Long id, Map<String, Object> aprobado);
	List<RdgDetalleDto.Response> findByIdRdgByAgregadoResponse(Long idRdg, Estado estado);
	RdgDetalleModel updateOrCreate(RdgDetalleDto.Response response, CuentaModel cuenta, RdgModel rdg,
			Map<String, Object> mapa);
	RdgDetalleDto.Publico getDocumentoPublico(String idDetalle);

}
