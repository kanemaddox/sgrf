package com.saims.sgrf.service.impl;

import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.UsuarioDao;
import com.saims.sgrf.dto.LoginDto;
import com.saims.sgrf.dto.PersonaDto;
import com.saims.sgrf.dto.SettingsDto;
import com.saims.sgrf.dto.UserDto;
import com.saims.sgrf.enums.Rol;
import com.saims.sgrf.model.UsuarioModel;
import com.saims.sgrf.service.SettingsService;
import com.saims.sgrf.service.authenticationService;
import com.saims.sgrf.service.jwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class authenticationServiceImpl implements authenticationService{
	
	private final UsuarioDao usuarioDao;
	private final jwtService jwtService;
	private Personalizer personalizer = new Personalizer();
	private final SettingsService settingsService;

	
	public LoginDto.Response login(LoginDto.Request request){
		String password ="";
		try {
			password = this.personalizer.getPass(request.getPass());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LoginDto.Response response = new LoginDto.Response();
		response.setUser("0");
		
		String idp = this.personalizer.normalizer(request.getUser()); 
		
		UsuarioModel usuario = this.usuarioDao.getUsuario(idp, password)
				.orElseGet(()->{
					UsuarioModel nuevo = new UsuarioModel();
					nuevo.setEstado(false);
					return nuevo;
				});
		
		
		if(usuario.getEstado()) {
			SettingsDto.Response settings = this.settingsService.getValue(1L);
			String idpLabel = (String) settings.getValue().get("idp");
			usuario.setIntentos(0);
			this.usuarioDao.save(usuario);
			
			response.setIdUsuario(usuario.getId());
			
			response.setEmail(usuario.getPersona().getEmail());
			response.setUser(usuario.getUsuario());
			response.setNombre(usuario.getPersona().getNombre());
			response.setImagen(usuario.getPersona().getFoto());
			response.setPass((usuario.getUsuario().equals(request.getPass()))?true:false);
			response.setPernicions(usuario.getPernicions());
			response.setRol(usuario.getRol());
			response.setIdpName(idpLabel);
		
			
			PersonaDto.Response persona = new PersonaDto.Response();
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
			
			Date expiration = jwtService.extractExpiration(jwt);

			long expiresAt = expiration.getTime();
			long expiresIn = expiresAt - System.currentTimeMillis();

			response.setToken(jwt);
			response.setRefresh(refresh);
			response.setExpiresAt(expiresAt);
			response.setExpiresIn(expiresIn);
		}
		else {
			
			usuario = this.usuarioDao.findByUsuario(request.getUser())
					.orElseGet(()->{
						UsuarioModel nuevo = new UsuarioModel();
						nuevo.setEstado(false);
						nuevo.setUsuario("no-----////");
						return nuevo;
					});
			if(usuario.getUsuario().equals(request.getUser())){
				usuario.setIntentos(usuario.getIntentos()+1);
				if(usuario.getIntentos()>=3)
					usuario.setEstado(false);
				this.usuarioDao.save(usuario);
				response.setIntentos(usuario.getIntentos());
			}
			else {
				response.setNombre("El Usuario NO Existe");
			}
				
		}
		return response;
	}
}
