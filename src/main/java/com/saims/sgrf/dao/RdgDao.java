package com.saims.sgrf.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.model.RdgModel;
import com.saims.sgrf.model.SrfModel;

public interface RdgDao extends JpaRepository<RdgModel,Long>{
	
	@Query(value="select * from rdg where id_srf=?",nativeQuery=true)
	Optional<RdgModel>findBySrf(Long idSrf);
	
	@Query(value = "select count(id_empleado) from rdg where id_empleado=? and gestion = ?",nativeQuery=true)
	int countNumero (Long idEmpleado,int gestion);
	
	@Query(value="select * from rdg where id_empleado=? and estado=?",nativeQuery=true)
	List<RdgModel>findByIdEmpleadoEstado(Long idEmpleado, String estado);
	
	@Query(value="select * from rdg where id=? and id_empleado = ?",nativeQuery=true)
	Optional<RdgModel>findByRdg(Long idRdg, Long idEmpleado);
	
	@Query(value="select * from rdg where token=? and id_empleado = ?",nativeQuery=true)
	Optional<RdgModel>findByTokenIdEmpleado(String token, Long idEmpleado);
	
	@Query(value="select * from rdg where token=?",nativeQuery=true)
	Optional<RdgModel>findByToken(String token);
	
	@Query(value="select * from rdg where id=? and id_srf=?",nativeQuery=true)
	Optional<RdgModel>findByIdRdgByIdSrf(Long idRdg, Long idSrf);
	
	@Query(value="select * from rdg where id=? and id_srf=? and id_empleado=? and token=?",nativeQuery=true)
	Optional<RdgModel>getRdgSolicitud(Long idRdg, Long idSrf, Long idEmpleado, String token);
	
	@Query(value = "select * from rdg where id_empleado=?", nativeQuery = true)
	List<RdgModel> findByIdEstado(Long idEmpleado);

}
