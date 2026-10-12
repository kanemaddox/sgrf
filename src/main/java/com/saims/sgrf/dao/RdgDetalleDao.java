package com.saims.sgrf.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.model.RdgDetalleModel;

public interface RdgDetalleDao extends JpaRepository<RdgDetalleModel,Long>{
	
	
	@Query(value="select * from rdg_detalle where id_rdg=?",nativeQuery=true)
	List<RdgDetalleModel>findByIdRdg(Long idRdg);
	
	@Query(value="select * from rdg_detalle where id_rdg=? and agregado = ?",nativeQuery=true)
	List<RdgDetalleModel>findByIdRdgByAgregado(Long idRdg, String agregado);
	
	@Query(value="select * from rdg_detalle where id=? and token = ?",nativeQuery=true)
	Optional<RdgDetalleModel>findByIdByToken(Long idDetalle, String agregado);
	
	@Query(value="select * from rdg_detalle where token=? and agregado = ?",nativeQuery=true)
	Optional<RdgDetalleModel>findByTokenAgregado(String token, String agregado);

}
