package com.saims.sgrf.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.model.DepartamentoModel;

public interface DepartamentoDao extends JpaRepository<DepartamentoModel, Long>{
	
	
	Optional<DepartamentoModel>findByNombre(String nombre);

}
