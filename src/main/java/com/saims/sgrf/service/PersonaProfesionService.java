package com.saims.sgrf.service;

import java.util.List;

import com.saims.sgrf.dto.PersonaDto;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.model.PersonaProfesionModel;
import com.saims.sgrf.model.ProfesionModel;

public interface PersonaProfesionService {
	PersonaProfesionModel createOrGet(PersonaModel persona, ProfesionModel profesion);
	List<PersonaProfesionModel> findAllModel();
	PersonaProfesionModel getPersonaProfesionActivo (Long idPersona);
	List<PersonaDto.Profesion> getPersonaProfesionUsuario (Long idUsuario);

}
