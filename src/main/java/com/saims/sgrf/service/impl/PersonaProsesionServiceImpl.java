package com.saims.sgrf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.saims.sgrf.dao.PersonaProfesionDao;
import com.saims.sgrf.dao.UsuarioDao;
import com.saims.sgrf.dto.PersonaDto;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.model.PersonaProfesionModel;
import com.saims.sgrf.model.ProfesionModel;
import com.saims.sgrf.model.UsuarioModel;
import com.saims.sgrf.service.PersonaProfesionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonaProsesionServiceImpl implements PersonaProfesionService{
	private final PersonaProfesionDao personaProfesionDao;
	private final UsuarioDao usuarioDao;
	@Transactional
	public PersonaProfesionModel createOrGet(PersonaModel persona, ProfesionModel profesion) {
		
		List<PersonaProfesionModel> lista = this.personaProfesionDao.getPersonaProfesion(persona.getId());
		PersonaProfesionModel personaProfesion = new PersonaProfesionModel();
		personaProfesion.setEstado(false);
		for(PersonaProfesionModel value : lista) {
			if(value.getProfesion().getId().longValue()==profesion.getId().longValue()) {
				value.setEstado(true);
				personaProfesion = value;
			}
			else
				value.setEstado(false);
			this.personaProfesionDao.save(value);
		}
		
		if(!personaProfesion.getEstado()) {
			personaProfesion.setPersona(persona);
			personaProfesion.setProfesion(profesion);
			personaProfesion.setEstado(true);
			this.personaProfesionDao.save(personaProfesion);
		}
		
		return personaProfesion;
	}
	
	@Transactional
	public List<PersonaProfesionModel> findAllModel() {
		List<PersonaProfesionModel> personaProfesion = this.personaProfesionDao.findAll();
		return personaProfesion;
	}
	
	@Transactional
	public PersonaProfesionModel getPersonaProfesionActivo (Long idPersona) {
		
		PersonaProfesionModel value = this.personaProfesionDao.getPersonaProfesionActivo(idPersona)
				.orElseGet(()->{
					PersonaProfesionModel nuevo = new PersonaProfesionModel();
					nuevo.setId(0L);
					return nuevo;
				});
		return value;
	}
	
	@Transactional
	public List<PersonaDto.Profesion> getPersonaProfesionUsuario (Long idUsuario) {
		UsuarioModel usuario = this.usuarioDao.getId(idUsuario);
		List<PersonaProfesionModel> lista = this.personaProfesionDao.getPersonaProfesion(usuario.getPersona().getId());
		List<PersonaDto.Profesion> profesiones = new ArrayList<PersonaDto.Profesion>();
		PersonaDto.Profesion response;
		for(PersonaProfesionModel value: lista) {
			response = new PersonaDto.Profesion();
			response.setId(value.getId());
			response.setProfesion(value.getProfesion().getNombre());
			response.setEstado(value.getEstado());
			response.setFechaCreacion(value.getFechaCreacion().toLocalDate().toString());
			response.setFechaActualizacion(value.getFechaActualizacion().toLocalDate().toString());
			profesiones.add(response);
		}
		return profesiones;
	}

}
