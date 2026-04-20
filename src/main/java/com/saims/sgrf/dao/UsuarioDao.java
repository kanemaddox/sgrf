package com.saims.sgrf.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.model.UsuarioModel;

public interface UsuarioDao extends JpaRepository<UsuarioModel,Long>{
	
	@Query(value = "select * from usuario where id=?",nativeQuery=true)
	UsuarioModel getId(Long id);
	
	@Query(value = "select * from usuario where usuario=? and password=? and estado=true",nativeQuery=true)
	Optional<UsuarioModel> getUsuario(String usuario, String password);
	
	Optional<UsuarioModel>findByUsuario(String usuario);
	Optional<UsuarioModel>findByEstado(boolean estado);
	
	@Query(value = "select * from usuario where estado=?",nativeQuery=true)
	List<UsuarioModel>findEstado(boolean estado);

}
