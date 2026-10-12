package com.saims.sgrf.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.model.NivelModel;

public interface NivelDao extends JpaRepository<NivelModel, Long> {
	
	@Query(value="select * from nivel where id_padre=? and id_hijo=?",nativeQuery=true)
	Optional<NivelModel>findByIdPadreByIdHijo(Long idPadre, Long idHijo);
	
	@Query(value="select * from nivel where id_hijo=?",nativeQuery=true)
	Optional<NivelModel>findByIdHijo(Long idHijo);
	

}
