package com.saims.sgrf.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.model.AreaModel;

public interface AreaDao extends JpaRepository<AreaModel, Long>{
	// nativo
	//@Query(value = "select * from area where nombre=?",nativeQuery=true)
	//AreaModel getArea(String nombre);
	
	// Jpa automatizado
	Optional<AreaModel>findByCodigo(String codigo);
	
	Optional<AreaModel>findByEstado(boolean estado);
	
	@Query(value = "select * from area where id=?",nativeQuery=true)
	AreaModel getId(Long id);
	
	@Query(value = "select * from area where tipo=?",nativeQuery=true)
	List<AreaModel> findByTipo(String tipo);
	
}
