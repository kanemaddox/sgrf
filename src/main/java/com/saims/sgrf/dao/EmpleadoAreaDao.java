package com.saims.sgrf.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.model.EmpleadoAreaModel;

public interface EmpleadoAreaDao extends JpaRepository<EmpleadoAreaModel, Long> {

	@Query(value = "select * from empleado_area where id_empleado=? and id_area=?", nativeQuery = true)
	Optional<EmpleadoAreaModel> findByIdEmpleadoByIdArea(Long idEmpleado, Long idArea);
	
	@Query(value ="select * from empleado_area where id_empleado=?",nativeQuery = true)
	List<EmpleadoAreaModel>getListaEmpleado(Long idEmpleado);
	
	@Query(value ="select * from empleado_area where id_empleado=? and estado=true",nativeQuery = true)
	Optional<EmpleadoAreaModel> getEmpleado(Long idEmpleado);

}
