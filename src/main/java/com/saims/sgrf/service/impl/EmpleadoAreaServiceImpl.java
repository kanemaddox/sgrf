package com.saims.sgrf.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.sgrf.dao.EmpleadoAreaDao;
import com.saims.sgrf.model.AreaModel;
import com.saims.sgrf.model.EmpleadoAreaModel;
import com.saims.sgrf.model.EmpleadoModel;
import com.saims.sgrf.service.EmpleadoAreaService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpleadoAreaServiceImpl implements EmpleadoAreaService{
	
	private final EmpleadoAreaDao empleadoAreaDao;
	private final ModelMapper modelMapper;
	
	@Transactional
	public EmpleadoAreaModel createOrGet (EmpleadoModel empleado, AreaModel area) {
		
		EmpleadoAreaModel empleadoArea = this.empleadoAreaDao.findByIdEmpleadoByIdArea(empleado.getId(),area.getId())
				.orElseGet(()->{
					List<EmpleadoAreaModel>lista = this.empleadoAreaDao.getListaEmpleado(empleado.getId());
					for(EmpleadoAreaModel value:lista) {
						value.setEstado(false);
						this.empleadoAreaDao.save(value);
					}
					
					EmpleadoAreaModel nuevo = new EmpleadoAreaModel();
					nuevo.setArea(area);
					nuevo.setEmpleado(empleado);
					nuevo.setEstado(true);
					this.empleadoAreaDao.save(nuevo);
					return nuevo;
				});
				
		return empleadoArea;
		
	}
	
	// funcion que retorna el aera de trabajo de un empleado que esta activo
	@Transactional
	public EmpleadoAreaModel getEmpleado (Long IdEmpleado) {
		
		EmpleadoAreaModel empleadoArea = this.empleadoAreaDao.getEmpleado(IdEmpleado)
				.orElseGet(()->{
					EmpleadoAreaModel nuevo = new EmpleadoAreaModel();
					nuevo.setId(0L);
					return nuevo;
				});
				
		return empleadoArea;
		
	}
}
