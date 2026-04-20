package com.saims.sgrf.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.model.ProfesionModel;

public interface ProfesionDao extends JpaRepository<ProfesionModel,Long>{
	@Query(value = "select * from profesion where nombre=?",nativeQuery=true)
	ProfesionModel getProfesion(String nombre);
	
	Optional<ProfesionModel> findByNombre(String nombre);

}
