package com.saims.sgrf.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.saims.sgrf.dao.EmpleadoSucursalDao;
import com.saims.sgrf.model.AreaModel;
import com.saims.sgrf.model.CentroCostosModel;
import com.saims.sgrf.model.EmpleadoModel;
import com.saims.sgrf.model.EmpleadoSucursalModel;
import com.saims.sgrf.model.SucursalModel;
import com.saims.sgrf.service.EmpleadoSucursalService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpleadoSucursalServiceImpl implements EmpleadoSucursalService{
	private final EmpleadoSucursalDao empleadoSucursalDao;
	
	@Transactional
	public EmpleadoSucursalModel addEmpleadoSucursal(EmpleadoModel empleado, SucursalModel sucursal,AreaModel area, CentroCostosModel centro) {
		List<EmpleadoSucursalModel> lista = this.empleadoSucursalDao.getSucursales(empleado.getId());
		for(EmpleadoSucursalModel value : lista) {
			value.setEstado(false);
			this.empleadoSucursalDao.save(value);
		}
		
		EmpleadoSucursalModel nuevo = new EmpleadoSucursalModel();
		nuevo.setEmpleado(empleado);
		nuevo.setSucursal(sucursal);
		nuevo.setArea(area);
		nuevo.setCentrocostos(centro);
		nuevo.setEstado(true);
		
		this.empleadoSucursalDao.save(nuevo);
		return nuevo;
	}
	@Transactional
	public EmpleadoSucursalModel getSucursalActivo(Long idEmpleado) {
		EmpleadoSucursalModel sucursal = this.empleadoSucursalDao.getSucursalActivo(idEmpleado);
		return sucursal;
	}
	

}
