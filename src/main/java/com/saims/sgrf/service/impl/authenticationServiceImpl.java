package com.saims.sgrf.service.impl;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.UsuarioDao;
import com.saims.sgrf.dto.LoginDtoRequest;
import com.saims.sgrf.dto.LoginDtoResponse;
import com.saims.sgrf.dto.PersonaDtoResponse;
import com.saims.sgrf.dto.UserDto;
import com.saims.sgrf.enums.Rol;
import com.saims.sgrf.model.UsuarioModel;
import com.saims.sgrf.service.authenticationService;
import com.saims.sgrf.service.jwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class authenticationServiceImpl implements authenticationService{
	
	private final UsuarioDao usuarioDao;
	private final jwtService jwtService;
	private Personalizer personalizer = new Personalizer();

	
	public LoginDtoResponse login(LoginDtoRequest request){
		String password ="";
		try {
			password = this.personalizer.getPass(request.getPass());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LoginDtoResponse response = new LoginDtoResponse();
		response.setUser("0");
		
		String idp = this.personalizer.normalizer(request.getUser()); 
		
		UsuarioModel usuario = this.usuarioDao.getUsuario(idp, password)
				.orElseGet(()->{
					UsuarioModel nuevo = new UsuarioModel();
					nuevo.setEstado(false);
					return nuevo;
				});
		
		
		if(usuario.getEstado()) {
			response.setIdUsuario(usuario.getId());
			
			response.setEmail(usuario.getPersona().getEmail());
			response.setUser(usuario.getUsuario());
			response.setNombre(usuario.getPersona().getNombre());
			response.setImagen(usuario.getImagen());
			
			PersonaDtoResponse persona = new PersonaDtoResponse();
			persona.setId(usuario.getPersona().getId());
			persona.setIdp(usuario.getPersona().getIdp());
			persona.setNombre(usuario.getPersona().getNombre());
			persona.setPaterno(usuario.getPersona().getPaterno());
			persona.setMaterno(usuario.getPersona().getMaterno());
			persona.setCelular(usuario.getPersona().getCelular());
			persona.setEmail(usuario.getPersona().getEmail());
			persona.setFechaNacimiento(usuario.getPersona().getFechaNacimiento());
			persona.setSexo(usuario.getPersona().getSexo());
			
			response.setPersona(persona);
			
			UserDto user = new UserDto();
			
			user.setUsername(response.getUser());
			user.setPassword(password);
			user.setRol(Rol.USER);
			
			var jwt = jwtService.generateToken(user);
			var refresh = jwtService.generateRefreshToken(new HashMap<>(),user);
			response.setToken(jwt);
			response.setRefresh(refresh);
		}				
		return response;
	}
}
