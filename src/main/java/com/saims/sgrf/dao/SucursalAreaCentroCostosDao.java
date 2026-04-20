package com.saims.sgrf.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.saims.sgrf.model.CuentaBancariaModel;
import com.saims.sgrf.model.SucursalAreaCentroCostosModel;

public interface SucursalAreaCentroCostosDao extends JpaRepository<SucursalAreaCentroCostosModel,Long> {

	//@Query(value = "select * from sucursal_area_centrocostos where id_sucursal=? and id_area=? and id_centrocostos=?",nativeQuery=true)
	//SucursalAreaCentroCostosModel getSucursalAreaCentroCostos(Long idSucursal, Long idArea, Long idCentroCostos);
	
	Optional<SucursalAreaCentroCostosModel> findBySucursal_IdAndArea_IdAndCentrocostos_Id(Long sucursalId, Long areaId, Long centrocostosId);
	
	@Query(value = """
		    SELECT * 
		    FROM sucursal_area_centrocostos 
		    WHERE id_sucursal = :idSucursal
		      AND id_area = :idArea
		      AND (:idCentroCostos IS NULL OR id_centrocostos = :idCentroCostos)
		      AND (id_centrocostos IS NULL OR :idCentroCostos IS NOT NULL)
		""", nativeQuery = true)
		Optional<SucursalAreaCentroCostosModel> getSucursalAreaCentroCostos(
		    @Param("idSucursal") Long idSucursal,
		    @Param("idArea") Long idArea,
		    @Param("idCentroCostos") Long centroCostos
		);
	
	List<SucursalAreaCentroCostosModel>findBySucursalId(Long id_surusal);
	
	List<SucursalAreaCentroCostosModel>findByAreaId(Long id_area);
	
	List<SucursalAreaCentroCostosModel>findByCentrocostosId(Long id_centrocostos);
	
	@Query("""
		    SELECT sac FROM SucursalAreaCentroCostosModel sac
		    JOIN FETCH sac.area
		    JOIN FETCH sac.sucursal
		    JOIN FETCH sac.centrocostos
		""")
	List<SucursalAreaCentroCostosModel> getSucursalesAreasCentroCostos();
	
}
