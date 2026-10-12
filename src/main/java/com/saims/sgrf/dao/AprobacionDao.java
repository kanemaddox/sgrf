package com.saims.sgrf.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.SrfAprobacionModel;

public interface AprobacionDao extends JpaRepository<SrfAprobacionModel,Long>{
	@Query(value="select * from aprobacion_srf where id_srf=?",nativeQuery=true)
	List<SrfAprobacionModel>findByIdSrf(Long idSrf);
	
	@Query(value="select * from aprobacion_srf where id_persona=?",nativeQuery=true)
	List<SrfAprobacionModel>findByIdPersona(Long idPersona);
	
	@Query(value="select * from aprobacion_srf where id_persona=? and estado=?",nativeQuery=true)
	List<SrfAprobacionModel>getPersonaEstado(Long idPersona, String estado);
	
	@Query(value="select * from aprobacion_srf where id=? and id_persona=?",nativeQuery=true)
	Optional<SrfAprobacionModel> getIdAprIdPersona(Long idApr,Long idPersona);
	
	@Query(value="select * from aprobacion_srf where token=? and id_persona=?",nativeQuery=true)
	Optional<SrfAprobacionModel> getTokenIdPersona(String token,Long idPersona);

}
