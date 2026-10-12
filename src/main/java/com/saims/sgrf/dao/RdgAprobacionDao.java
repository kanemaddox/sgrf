package com.saims.sgrf.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.model.RdgAprobacionModel;

public interface RdgAprobacionDao extends JpaRepository<RdgAprobacionModel,Long>{
	
	@Query(value="select * from aprobacion_rdg where id_rdg=?",nativeQuery=true)
	List<RdgAprobacionModel>findByIdRdg(Long idRdg);
	
	@Query(value="select * from aprobacion_rdg where id_persona=? and estado=?",nativeQuery=true)
	List<RdgAprobacionModel>findByIdPersonaByEstado(Long idPersona, String estado);
	
	@Query(value="select * from aprobacion_rdg where id = ? and id_persona=?",nativeQuery=true)
	Optional<RdgAprobacionModel>findByIdPersona(Long id,Long idPersona);
	
	@Query(value="select * from aprobacion_rdg where token = ? and id_persona=?",nativeQuery=true)
	Optional<RdgAprobacionModel>findByIdPersonaByToken(String token,Long idPersona);
	
	@Query(value="select * from aprobacion_rdg where id = ? and id_persona=? and id_rdg = ?",nativeQuery=true)
	Optional<RdgAprobacionModel>findByIdPersonaByIdRdg(Long idAprobacion,Long idPersona, Long idRdg);
	
	@Query(value="select * from aprobacion_rdg where id_persona=?",nativeQuery=true)
	List<RdgAprobacionModel>findByIdPersona(Long idPersona);

}
