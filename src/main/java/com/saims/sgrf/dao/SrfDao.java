package com.saims.sgrf.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.model.SrfModel;

public interface SrfDao extends JpaRepository<SrfModel, Long> {
	@Query(value = "select count(id_empleado) from srf where id_empleado=? and gestion = ?", nativeQuery = true)
	int countNumero(Long idEmpleado, int gestion);

	@Query(value = "select * from srf where id_empleado=? and estado=?", nativeQuery = true)
	List<SrfModel> findByIdEmpleadoEstado(Long idEmpleado, String estado);
	
	@Query(value = "select * from srf where id_empleado=?", nativeQuery = true)
	List<SrfModel> findByIdEstado(Long idEmpleado);

	@Query(value = "select * from srf where id=? and id_empleado=? ", nativeQuery = true)
	Optional<SrfModel> findBySrf(Long idSrf, Long idEmpleado);
	
	@Query(value = "select * from srf where token=? and id_empleado=? ", nativeQuery = true)
	Optional<SrfModel> findByToken(String token, Long idEmpleado);
	
	@Query(value = "select * from srf where token=?", nativeQuery = true)
	Optional<SrfModel> findByToken(String token);

}
