package com.saims.sgrf.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.enums.TipoEmpleado;
import com.saims.sgrf.model.EmpleadoModel;
public interface EmpleadoDao extends JpaRepository<EmpleadoModel,Long>{
	
	@Query(value = "select * from empleado where id_persona=?",nativeQuery=true)
	EmpleadoModel getEmpleado(Long idPersona);
	
	Optional<EmpleadoModel> findByPersona_Id(Long personaId);
	
	List<EmpleadoModel> findByTipo(TipoEmpleado tipo);
	
	List<EmpleadoModel> findByEstado(boolean estado);

}
