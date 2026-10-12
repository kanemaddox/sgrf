package com.saims.sgrf.service;

import java.util.List;

import com.saims.sgrf.dto.DatosDto;

public interface DatosService {
	
	DatosDto.Response AddDatos(DatosDto.Request request) throws Exception;
	List<DatosDto.Request>getEmpleadosFile();
	void setAllEstado(boolean estado);
	DatosDto.Response getDatos(Long idPersona);
	DatosDto.Response getDatosUsuario(Long idUsuario);
	
	DatosDto.Empleado getDatosEmpleados();
	
	DatosDto.Perfil getPerfil(Long idPersona);

}
