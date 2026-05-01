package com.saims.sgrf.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.UsuarioDao;
import com.saims.sgrf.dto.UserDto;
import com.saims.sgrf.dto.UsuarioDtoRequest;
import com.saims.sgrf.dto.UsuarioDtoResponse;
import com.saims.sgrf.dto.passwordDtoRequest;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.enums.Rol;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.model.UsuarioModel;
import com.saims.sgrf.service.UsuarioService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
	private final UsuarioDao usuarioDao;
	private final ModelMapper modelMapper;
	private Personalizer personalizer = new Personalizer(); 
	
	@Override
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) {
				String idp = personalizer.normalizer(username);
				UserDto user = new UserDto();
				UsuarioModel usuario = usuarioDao.findByUsuario(idp)
						.orElseGet(()->{
							UsuarioModel us = new UsuarioModel();
							us.setEstado(false);
							return us;
						});
				if(usuario.getEstado()) {
					user.setUsername(usuario.getUsuario());
					user.setPassword(usuario.getPassword());
					user.setRol(Rol.USER);
				}
				return user;
				/*return userRepository.findByEmail(username)
						.orElseThrow(()-> new UsernameNotFoundException("User not Found"));*/
			}
		};
	}
	
	@Transactional
	public UsuarioModel createOrGet(PersonaModel persona) throws Exception {
		
		String pass = this.personalizer.password(persona.getIdp());
		
		UsuarioModel usuario = this.usuarioDao.findByUsuario(persona.getIdp())
				.map(e->{
					e.setEstado(true);
					return(e);
				})
				.orElseGet(()->{
					UsuarioModel nuevo = new UsuarioModel();
					nuevo.setUsuario(persona.getIdp());
					nuevo.setPassword(pass);
					nuevo.setEstado(true);
					nuevo.setPersona(persona);
					nuevo.setImagen("usuario.png");
					return (nuevo);
				});
		this.usuarioDao.save(usuario);		
		return usuario;
	}
	/**
	 * funciones para retornar el usuario mediante su id
	 * retorno mediante Response y Model 
	 */
	@Transactional
	public UsuarioDtoResponse getUsuarioResponse(Long id) {
		UsuarioModel usuario = this.usuarioDao.getId(id);
		return (this.modelToResponse(usuario));
	}
	
	@Transactional
	public UsuarioModel getUsuarioModel(Long id) {
		UsuarioModel usuario = this.usuarioDao.getId(id);
		return (usuario);
	}
	
	@Transactional
	public void setAllEstado(boolean estado) {
		List<UsuarioModel>usuarios = this.usuarioDao.findAll();
		usuarios.forEach(u ->{
			u.setEstado(estado);
		});
		
		this.usuarioDao.saveAll(usuarios);
	}
	/**
	 * funsion para el retorno de una lista de Usuarios 
	 * @param estado
	 * @return lista de Usuarios Response y Model
	 */
	@Transactional
	public List<UsuarioDtoResponse> findAllResponse(Estado estado){
		List<UsuarioDtoResponse> usuarios = null;
		switch(estado) {
			case ACTIVO:
				usuarios  = this.usuarioDao.findByEstado(true).stream()
				.map(usuario -> this.modelToResponse(usuario))
				.collect(Collectors.toList());
			break;
			case INACTIVO:
				usuarios  = this.usuarioDao.findByEstado(false).stream()
				.map(usuario -> this.modelToResponse(usuario))
				.collect(Collectors.toList());
				break;
			case TODOS:
				usuarios = this.usuarioDao.findAll().stream()
				.map(usuario->this.modelToResponse(usuario))
				.collect(Collectors.toList());
				break;
		}
		return usuarios;
	}
	@Transactional
	public List<UsuarioModel>findAllModel(Estado estado){
		List<UsuarioModel> usuarios = null;
		switch(estado) {
			case ACTIVO:
				usuarios  = this.usuarioDao.findEstado(true);
				break;
			case INACTIVO:
				usuarios  = this.usuarioDao.findEstado(false);
				break;
			case TODOS:
				usuarios = this.usuarioDao.findAll();
				break;
		}
		return usuarios;
	}
	
	@Transactional
	public boolean updateUsuarioPassword(passwordDtoRequest request)throws Exception {
		UsuarioModel usuario = this.usuarioDao.getId(request.getId());
		if(usuario.getPassword().equals(this.personalizer.password(request.getPass1()))) {
			usuario.setPassword(this.personalizer.password(request.getPass3()));
			this.usuarioDao.save(usuario);
			return (true);
		}
		else
			return(false);
	}
	
	@Transactional
	public UsuarioDtoResponse updateUsuarioImagen(UsuarioDtoResponse response) {
		UsuarioModel usuario = this.usuarioDao.getId(response.getId());	
		usuario.setImagen(response.getImagen());
		this.usuarioDao.save(usuario);
		return this.modelToResponse(usuario);
	}
	
	@Transactional
	public UsuarioDtoResponse updatePassword(Long id) {
		UsuarioModel usuario = this.usuarioDao.getId(id);
		String password = "0";
		try {
			password = this.personalizer.password(usuario.getUsuario());
			usuario.setPassword(password);
			this.usuarioDao.save(usuario);
			return (this.modelToResponse(usuario));
		}catch(Exception e) {
			password = "0";
			return null;
		}
	}
	
	/**
	 * 
	 * @param usuario
	 * @return Response de Usuario
	 */
	private UsuarioDtoResponse modelToResponse(UsuarioModel usuario) {
		return (this.modelMapper.map(usuario, UsuarioDtoResponse.class));
	}
}
