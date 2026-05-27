package com.saims.sgrf.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.model.EmpleadoSucursalModel;

public interface EmpleadoSucursalDao extends JpaRepository<EmpleadoSucursalModel,Long>{

	@Query(value="select * from empleado_sucursal where id_empleado = ?",nativeQuery=true)
	List<EmpleadoSucursalModel>getSucursales (Long idEmpleado);
	
	@Query(value="select * from empleado_sucursal where id_empleado = ? and estado=true",nativeQuery=true)
	EmpleadoSucursalModel getSucursalActivo(Long idEmpleado);
	
	
}
