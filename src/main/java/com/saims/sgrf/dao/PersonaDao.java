package com.saims.sgrf.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.model.PersonaModel;

public interface PersonaDao extends JpaRepository<PersonaModel,Long>{
	
	@Query(value = "select * from persona where idp=?",nativeQuery=true)
	PersonaModel getIdp(String idp);
	
	Optional<PersonaModel> findByIdp(String idp);
	
	@Query(value = "select * from persona where id=?",nativeQuery=true)
	PersonaModel getId(Long id);

}
