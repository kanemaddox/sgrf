
package com.saims.sgrf.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.saims.sgrf.dto.UsuarioDtoResponse;
import com.saims.sgrf.dto.passwordDtoRequest;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.model.UsuarioModel;

public interface UsuarioService{
	UsuarioModel createOrGet(PersonaModel persona) throws Exception;
	UsuarioDtoResponse getUsuarioResponse(Long id);
	UsuarioModel getUsuarioModel(Long id);
	List<UsuarioDtoResponse> findAllResponse(Estado estado);
	List<UsuarioModel>findAllModel(Estado estado);
	
	void setAllEstado(boolean estado);
	
	UserDetailsService userDetailsService();
	
	UsuarioDtoResponse updateUsuarioImagen(UsuarioDtoResponse response);
	UsuarioDtoResponse updatePassword(Long id);
	
	boolean updateUsuarioPassword(passwordDtoRequest request)throws Exception;
}
