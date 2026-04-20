package com.saims.sgrf.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.sgrf.model.CuentaBancariaModel;

public interface CuentaBancariaDao extends JpaRepository<CuentaBancariaModel,Long> {
	
	@Query(value = "select * from cuentabancaria where id_persona=?",nativeQuery=true)
	List<CuentaBancariaModel> getListaCuentaBancaria(Long id_persona);
	
	@Query(value = "select * from cuentabancaria where id=?",nativeQuery=true)
	CuentaBancariaModel getId(Long id);
	
	@Query("""
		    SELECT c FROM CuentaBancariaModel c
		    JOIN FETCH c.persona
		    JOIN FETCH c.banco
		    WHERE c.estado = true
		""")
	List<CuentaBancariaModel> getCuentasBancarias();
	
	Optional<CuentaBancariaModel>findByEstado(boolean estado);

}
