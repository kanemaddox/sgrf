package com.saims.sgrf.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.model.CuentaModel;

public interface CuentaDao extends JpaRepository<CuentaModel,Long>{
	
	@Query(value = "select * from cuenta where cuenta=? and tipo=?",nativeQuery=true)
	Optional<CuentaModel> findByCodigoByTipo(String cuenta, String tipo);
	
	@Query(value = "select * from cuenta where categoria=?",nativeQuery=true)
	List<CuentaModel> findByCategoria(String categoria);

}
