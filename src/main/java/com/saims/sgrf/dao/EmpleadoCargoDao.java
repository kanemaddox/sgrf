package com.saims.sgrf.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.model.EmpleadoCargoModel;

public interface EmpleadoCargoDao extends JpaRepository<EmpleadoCargoModel, Long>{
	
	@Query(value = "select * from empleado_cargo where id_empleado=?",nativeQuery=true)
	List<EmpleadoCargoModel>getCargos(Long idEmpleado);
	
	@Query(value = "select * from empleado_cargo where id_empleado=? and estado=true",nativeQuery=true)
	EmpleadoCargoModel getCargoActivo(Long idEmpleado);
	
	List<EmpleadoCargoModel> findByEstado(boolean estado);

}
