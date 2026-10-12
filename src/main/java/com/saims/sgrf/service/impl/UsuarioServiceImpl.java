package com.saims.sgrf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.UsuarioDao;
import com.saims.sgrf.dto.SrfDto;
import com.saims.sgrf.dto.UserDto;
import com.saims.sgrf.dto.UsuarioDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.enums.Rol;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.model.UsuarioModel;
import com.saims.sgrf.service.PersonaService;
import com.saims.sgrf.service.SrfService;
import com.saims.sgrf.service.UsuarioService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
	private final UsuarioDao usuarioDao;
	private final ModelMapper modelMapper;
	private final PersonaService personaService;
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
		
		Map<String, Object> pernicions = new HashMap<>();
		pernicions.put("mi_cuenta",true);
		pernicions.put("perfil",true);
		pernicions.put("cuenta_bancaria",true);
		pernicions.put("formularios",true);
		pernicions.put("crear_solicitud",true);
		pernicions.put("crear_repocicion",true);
		pernicions.put("listar_solicitud",true);
		
		String pass = this.personalizer.password(persona.getIdp());
		
		UsuarioModel usuario = this.usuarioDao.findByUsuario(persona.getIdp())
				.map(e->{
					e.setEstado(true);
					e.setIntentos(0);
					e.setPernicions(pernicions);
					return(e);
				})
				.orElseGet(()->{
					UsuarioModel nuevo = new UsuarioModel();
					nuevo.setUsuario(persona.getIdp());
					nuevo.setPassword(pass);
					nuevo.setEstado(true);
					nuevo.setIntentos(0);
					nuevo.setPersona(persona);
					nuevo.setPernicions(pernicions);
					nuevo.setRol(Rol.USER);
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
	public UsuarioDto.Response getUsuarioResponse(Long id) {
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
		usuarios.get(0).setEstado(true);
		this.usuarioDao.saveAll(usuarios);
	}
	/**
	 * funsion para el retorno de una lista de Usuarios 
	 * @param estado
	 * @return lista de Usuarios Response y Model
	 */
	@Transactional
	public List<UsuarioDto.Response> findAllResponse(Estado estado){
		List<UsuarioDto.Response> usuarios = null;
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
		usuarios.removeIf(usuario -> usuario.getId().longValue() == 1L);
		return usuarios;
	}
	@Transactional
	public List<UsuarioDto.Response> findByRol(Rol rol){
		
		List<UsuarioDto.Response> usuarios  = this.usuarioDao.findByRol(rol).stream()
				.map(usuario -> this.modelToResponse(usuario))
				.collect(Collectors.toList());
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
	public boolean updateUsuarioPassword(UsuarioDto.Password password)throws Exception {
		UsuarioModel usuario = this.usuarioDao.getId(password.getId());
		if(usuario.getPassword().equals(this.personalizer.password(password.getPass1()))) {
			usuario.setPassword(this.personalizer.password(password.getPass3()));
			this.usuarioDao.save(usuario);
			return (true);
		}
		else
			return(false);
	}
	
	@Transactional
	public UsuarioDto.Response updateUsuarioImagen(UsuarioDto.Response response) {
		UsuarioModel usuario = this.usuarioDao.getId(response.getId());	
		usuario.getPersona().setFoto(response.getImagen());
		this.usuarioDao.save(usuario);
		return this.modelToResponse(usuario);
	}
	
	@Transactional
	public void updateUsuarioPernicions(UsuarioDto.Response response) {
		UsuarioModel usuario = this.usuarioDao.getId(response.getId());
		usuario.setPernicions(response.getPernicions());
		this.usuarioDao.save(usuario);
	}
	
	@Transactional
	public boolean updateUsuarioRol(UsuarioDto.UserRol rol) throws Exception {
		UsuarioModel usuario = this.usuarioDao.getId(rol.getIdAdmin());
		String pass = this.personalizer.password(rol.getPass());
		if(usuario.getPassword().equals(pass)) {
			usuario = this.usuarioDao.getId(rol.getIdUser());
			usuario.setRol(rol.getRol());
			this.usuarioDao.save(usuario);
			return true;
		}
		else {
			return false;
		}
	}
	
	@Transactional
	public boolean updatePassword(UsuarioDto.Password password) {
		
		UsuarioModel usuario = this.usuarioDao.getId(password.getId());
		String pass = "0";
		try {
			pass = this.personalizer.password(usuario.getUsuario());
			usuario.setPassword(pass);
			usuario.setEstado(true);
			usuario.setIntentos(0);
			this.usuarioDao.save(usuario);
			return (true);
		}catch(Exception e) {
			pass = "0";
			return false;
		}
	}
	
	@Transactional
	public UsuarioDto.Response getUsuarioValido(String user, String email) {
		
		UsuarioModel usuario = this.usuarioDao.findByUsuario(this.personalizer.normalizerIdp(user))
				.orElseGet(()->{
					UsuarioModel nuevo = new UsuarioModel();
					nuevo.setUsuario("0");
					return nuevo;
				});
		
		PersonaModel persona = this.personaService.findByEmail(email);
		UsuarioDto.Response response = new UsuarioDto.Response();
		response.setUsuario("");
		response.setPassword("");
		response.setEstado(false);
		if(!usuario.getUsuario().equals("0")) {
			response.setUsuario("El Usuario con "+ user+ " Existe");
		}
		if(!persona.getEmail().equals("0")) {
			response.setPassword("El Usuario con "+email+" Existe");
		}
		
		if(usuario.getUsuario().equals("0") && persona.getEmail().equals("0")) {
			response.setEstado(true);
		}
		return response;
	}
	
	/**
	 * 
	 * @param usuario
	 * @return Response de Usuari
	 */
	private UsuarioDto.Response modelToResponse(UsuarioModel usuario) {
		return (this.modelMapper.map(usuario, UsuarioDto.Response.class));
	}
}
