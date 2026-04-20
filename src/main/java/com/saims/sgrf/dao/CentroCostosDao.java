package com.saims.sgrf.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.model.CentroCostosModel;

public interface CentroCostosDao  extends JpaRepository<CentroCostosModel,Long>{
	
	@Query(value = "select * from centrocostos where codigo=?",nativeQuery=true)
	CentroCostosModel getCentroCostos(String codigoCentroCostos);
	
	Optional<CentroCostosModel> findByCodigo(String codigo);
	
	Optional<CentroCostosModel> findByNombre(String nombre);

}
