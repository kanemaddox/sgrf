package com.saims.sgrf.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.AprobacionModel;

public interface AprobacionDao extends JpaRepository<AprobacionModel,Long>{
	@Query(value="select * from aprobacion where id_srf=?",nativeQuery=true)
	List<AprobacionModel>findByIdSrf(Long idSrf);
	
	@Query(value="select * from aprobacion where id_persona=?",nativeQuery=true)
	List<AprobacionModel>findByIdPersona(Long idPersona);
	
	@Query(value="select * from aprobacion where id_persona=? and estado=?",nativeQuery=true)
	List<AprobacionModel>getPersonaEstado(Long idPersona, String estado);
	
	@Query(value="select * from aprobacion where id=? and id_persona=?",nativeQuery=true)
	Optional<AprobacionModel> getIdAprIdPersona(Long idApr,Long idPersona);

}
