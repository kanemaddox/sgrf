package com.saims.sgrf.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.model.PersonaProfesionModel;

public interface PersonaProfesionDao extends JpaRepository<PersonaProfesionModel,Long> {
	
	@Query(value = "SELECT * FROM persona_profesion WHERE id_persona = ?", nativeQuery = true)
	List<PersonaProfesionModel> getPersonaProfesion(Long idPersona);
	
	@Query(value = "SELECT * FROM persona_profesion WHERE id_persona = ? AND estado = true", nativeQuery = true)
	Optional<PersonaProfesionModel> getPersonaProfesionActivo(Long idPersona);

}
