package com.saims.sgrf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.EmpleadoCargoDao;
import com.saims.sgrf.dao.EmpleadoDao;
import com.saims.sgrf.dao.PersonaDao;
import com.saims.sgrf.dao.UsuarioDao;
import com.saims.sgrf.dto.DatosDto;
import com.saims.sgrf.dto.EmpleadoDto;
import com.saims.sgrf.model.CargoModel;
import com.saims.sgrf.model.EmpleadoCargoModel;
import com.saims.sgrf.model.EmpleadoModel;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.model.UsuarioModel;
import com.saims.sgrf.service.EmpleadoCargoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpleadoCargoServiceImpl implements EmpleadoCargoService{
	
	private final EmpleadoCargoDao empleadoCargoDao;
	private final UsuarioDao usuarioDao;
    private final EmpleadoDao empleadoDao;
    private final PersonaDao personaDao;
    
	private Personalizer personalizer = new Personalizer();
	@Transactional
	public EmpleadoCargoModel addEmpleadoCargo(EmpleadoModel empleado, CargoModel cargo,DatosDto.Request request) {
		
		EmpleadoCargoModel nuevo = null;
		
		PersonaModel jefe = this.personaDao.findByIdp(this.personalizer.normalizerIdp(request.getIdpJefe()))
				.orElseGet(()->{
					return this.personaDao.getId(1L);
				});	
		
		List<EmpleadoCargoModel> lista = this.empleadoCargoDao.getCargos(empleado.getId());
		
		if(!cargoActual(cargo,jefe,lista)) {
			for(EmpleadoCargoModel value:lista) {
				value.setEstado(false);
				this.empleadoCargoDao.save(value);
			}
			nuevo = new EmpleadoCargoModel();
			nuevo.setEmpleado(empleado);
			nuevo.setCargo(cargo);
			nuevo.setJefe(jefe);
			nuevo.setIdpPago(request.getIdpPago());
			nuevo.setEstado(true);
			this.empleadoCargoDao.save(nuevo);
		}		
		return nuevo;
	}
	@Transactional
	public EmpleadoCargoModel getCargoActivo(Long idEmplado) {
		EmpleadoCargoModel cargo = this.empleadoCargoDao.getCargoActivo(idEmplado); 
		return cargo;
	}
	
	@Transactional
	public List<EmpleadoCargoModel> findByEstado(boolean estado) {
		List<EmpleadoCargoModel> cargos = this.empleadoCargoDao.findByEstado(estado); 
		return cargos;
	}
	
	/**
     * 📌 Busca un cargo por ID USUARIO
     */
    @Transactional
    public List<EmpleadoDto.Cargo> getEmpleadoCargoUsuario(Long idUsuario) {
    	UsuarioModel persona = this.usuarioDao.getId(idUsuario);
    	EmpleadoModel empleado = this.empleadoDao.getEmpleado(persona.getPersona().getId());
    	List<EmpleadoCargoModel> lista = this.empleadoCargoDao.getCargos(empleado.getId());
    	List<EmpleadoDto.Cargo> cargos = new ArrayList<EmpleadoDto.Cargo>();
    	EmpleadoDto.Cargo response;
    	for(EmpleadoCargoModel value: lista) {
        	response = new EmpleadoDto.Cargo();
        	response.setId(value.getId());
        	response.setCargo(value.getCargo().getNombre());
        	response.setEstado(value.getEstado());
        	response.setIdpJefe(value.getJefe().getIdp());
        	response.setJefe(value.getJefe().getNombre() + " "+value.getJefe().getPaterno()+" "+value.getJefe().getMaterno());
        	response.setFechaCreacion(value.getFechaCreacion().toLocalDate().toString());
        	response.setFechaActualizacion(value.getFechaActualizacion().toLocalDate().toString());
        	cargos.add(response);
        }
        return cargos;
    }
    
    
    private boolean cargoActual(CargoModel cargo, PersonaModel jefe, List<EmpleadoCargoModel> lista) {
    	boolean estado = false;
    	for(EmpleadoCargoModel c: lista) {
    		if((c.getCargo().getId().longValue() == cargo.getId().longValue()) && (c.getJefe().getId().longValue()== jefe.getId().longValue())) {
    			estado = true;
    			break;
    		}
    	}
    	return estado;
    }
}
