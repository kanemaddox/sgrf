package com.saims.sgrf.service;

import java.util.List;

import com.saims.sgrf.dto.DatosDtoRequest;
import com.saims.sgrf.dto.DatosDtoResponse;

public interface DatosService {
	
	DatosDtoResponse AddDatos(DatosDtoRequest datosDtoRequest) throws Exception;
	List<DatosDtoResponse>getEmpleadosFile();
	void setAllEstado(boolean estado);

}
