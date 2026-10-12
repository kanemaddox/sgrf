package com.saims.sgrf.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.model.RdgDevolverModel;

public interface RdgDevolverDao extends JpaRepository<RdgDevolverModel,Long>{
	
	@Query(value="select * from rdg_devolver where id=? and token = ?",nativeQuery=true)
	Optional<RdgDevolverModel>findByIdByToken(Long idDevolver, String token);
	
	@Query(value="select * from rdg_devolver where id_rdg=? and agregado = ?",nativeQuery=true)
	List<RdgDevolverModel>findByIdRdgByAgregado(Long idRdg, String agregado);

}
