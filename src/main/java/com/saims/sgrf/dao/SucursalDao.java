package com.saims.sgrf.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.model.SucursalModel;

public interface SucursalDao extends JpaRepository<SucursalModel,Long>{
	@Query(value = "select * from sucursal where codigo=?",nativeQuery=true)
	SucursalModel getSucursal(String codigo);
	
	Optional<SucursalModel> findByCodigo(String codigo);
	
	Optional<SucursalModel>findByNombre(String nombre);
	
	@Query(value = "select * from sucursal where id=?",nativeQuery=true)
	SucursalModel getId(Long id);

}
