
package com.saims.sgrf.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.saims.sgrf.dto.UsuarioDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.enums.Rol;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.model.UsuarioModel;

public interface UsuarioService{
	UsuarioModel createOrGet(PersonaModel persona) throws Exception;
	UsuarioDto.Response getUsuarioResponse(Long id);
	UsuarioModel getUsuarioModel(Long id);
	List<UsuarioDto.Response> findAllResponse(Estado estado);
	List<UsuarioModel>findAllModel(Estado estado);
	
	void setAllEstado(boolean estado);
	
	UserDetailsService userDetailsService();
	
	UsuarioDto.Response updateUsuarioImagen(UsuarioDto.Response response);
	boolean updatePassword(UsuarioDto.Password password);
	
	boolean updateUsuarioPassword(UsuarioDto.Password password)throws Exception;
	boolean updateUsuarioRol(UsuarioDto.UserRol rol) throws Exception;
	
	UsuarioDto.Response getUsuarioValido(String usuario, String email);
	
	void updateUsuarioPernicions(UsuarioDto.Response response);
	
	List<UsuarioDto.Response> findByRol(Rol rol);
}
