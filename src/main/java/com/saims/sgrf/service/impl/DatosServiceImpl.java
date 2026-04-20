package com.saims.sgrf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.sgrf.dto.DatosDtoRequest;
import com.saims.sgrf.dto.DatosDtoResponse;
import com.saims.sgrf.model.AreaModel;
import com.saims.sgrf.model.BancoModel;
import com.saims.sgrf.model.CargoModel;
import com.saims.sgrf.model.CentroCostosModel;
import com.saims.sgrf.model.CuentaBancariaModel;
import com.saims.sgrf.model.EmpleadoModel;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.model.PersonaProfesionModel;
import com.saims.sgrf.model.ProfesionModel;
import com.saims.sgrf.model.SucursalAreaCentroCostosModel;
import com.saims.sgrf.model.SucursalModel;
import com.saims.sgrf.service.AreaService;
import com.saims.sgrf.service.BancoService;
import com.saims.sgrf.service.CargoService;
import com.saims.sgrf.service.CentroCostosService;
import com.saims.sgrf.service.CuentaBancariaService;
import com.saims.sgrf.service.DatosService;
import com.saims.sgrf.service.EmpleadoService;
import com.saims.sgrf.service.PersonaProfesionService;
import com.saims.sgrf.service.PersonaService;
import com.saims.sgrf.service.ProfesionService;
import com.saims.sgrf.service.SucursalAreaCentroCostosService;
import com.saims.sgrf.service.SucursalService;
import com.saims.sgrf.service.UsuarioService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DatosServiceImpl implements DatosService{
	
	private final ModelMapper modelMapper;
	
	private final PersonaService personaService;
	private final BancoService bancoService;
	private final CuentaBancariaService cuentaBancariaService;
	private final CargoService cargoService;
	private final ProfesionService profesionService;
	private final AreaService areaServise;
	private final CentroCostosService centroCostosService;
	private final SucursalService sucursalService;
	private final SucursalAreaCentroCostosService sucursalAreaCentroCostosService; 
	private final EmpleadoService empleadoService;
	private final UsuarioService usuarioService;
	private final PersonaProfesionService personaProfesionService;
	
	@Transactional
	public DatosDtoResponse AddDatos(DatosDtoRequest datosDtoRequest) throws Exception {
		
		/**
		 * Registro para persona
		 */
		//System.out.println("Creando Persona DatosService");
		PersonaModel personaModel = this.personaService.createOrGet(datosDtoRequest);
		this.usuarioService.createOrGet(personaModel);
		//System.out.println("Datos Creados "+personaModel.getNombre());
		
		/**
		 * Registro para banco
		 */
		
		System.out.println("Creando Banaco DatosService");
		BancoModel bancoModel = this.bancoService.createOrGet(datosDtoRequest); 
		System.out.println("DAtos Creados =========="+bancoModel.getNombre());
		/**
		 * Registro de cuenta Bancaria
		 */
		System.out.println("Creando Cuenta Bancaria DatosService");
		CuentaBancariaModel cuentaBancariaModel = this.cuentaBancariaService.createOrGet(personaModel, bancoModel, datosDtoRequest);
		System.out.println("Datos creados============"+ cuentaBancariaModel.getNumero());
		
		System.out.println("Creando Profesion DatosService");
		ProfesionModel profesionModel = this.profesionService.createOrGet(datosDtoRequest);
		System.out.println("Datos Creados ============"+profesionModel.getNombre());
		
		PersonaProfesionModel personaProfesionModel = this.personaProfesionService.createOrGet(personaModel, profesionModel);
		
		
		/**
		 * registro para cargo
		 */
		System.out.println("Creando Cargo DatosService");
		CargoModel cargoModel = this.cargoService.createOrGet(datosDtoRequest);
		System.out.println("Datos Creados ==========="+cargoModel.getNombre());
		/**
		 * registro para aprofesion
		 */
		
		/**
		 * registro de Area
		 */
		System.out.println("==================Creando Area DatosService");
		AreaModel areaModel = this.areaServise.createOrGet(datosDtoRequest);
		System.out.println("Datos Creados ============"+areaModel.getNombre());
	

		/**
		 * registro de Centro de costos
		 * verificamos si en el request el nombre es ninguno si es ninguno mandamos un centro de costos nulo
		 * caso contrario mandamos a la funcion addDatos 
		 */
		System.out.println("==================Creando Centro de Costos DatosService");
		CentroCostosModel centroCostosModel = null;
		if(!datosDtoRequest.getNombreCentroCostos().equals("Ninguno"))
			centroCostosModel = this.centroCostosService.createOrGet(datosDtoRequest);
		System.out.println("Datos Creados =================="+centroCostosModel.getNombre());
		/**
		 * registro de Sucursal
		 */
		
		System.out.println("==================Creando Sucursal DatosService");
		SucursalModel sucursalModel = this.sucursalService.createOrGet(datosDtoRequest);
		System.out.println("Datos Creados =================="+sucursalModel.getNombre());

		/**
		 * registro de relasion entre Sucursal y Area
		 */
		
		System.out.println("==================Creando Sucursal Area Centro de Costos DatosService");
		SucursalAreaCentroCostosModel sucursalAreaCentroCostosModel = this.sucursalAreaCentroCostosService.createOrGet(sucursalModel, areaModel, centroCostosModel);
		System.out.println("Datos Creados ============================ "+sucursalAreaCentroCostosModel.getId());
		//System.out.println(sucursalAreaCentroCostosModel.getFechaCreacion());
		/**
		 * registro de emplado de 
		 */
		
		System.out.println("==================Creando Empleado DatosService");
		EmpleadoModel empleadoModel = this.empleadoService.addDatos(personaModel, cargoModel, sucursalModel, datosDtoRequest); 
		System.out.println("Datos Creados ============= "+empleadoModel.getEstado());
		return null;
	}
	
	@Transactional
	public List<DatosDtoResponse>getEmpleadosFile(){
		
		List<CuentaBancariaModel>cuentas = this.cuentaBancariaService.getCuentasBancarias();
		List<SucursalAreaCentroCostosModel> sac = this.sucursalAreaCentroCostosService.findAll();
		List<EmpleadoModel> empleados = this.empleadoService.findAll();
		List<PersonaModel>personas = this.personaService.findAll();
		List<PersonaProfesionModel>profesiones = this.personaProfesionService.findAllModel();
			
		List<DatosDtoResponse>datos = new ArrayList<DatosDtoResponse>();
		
		cuentas.forEach(c->{
			DatosDtoResponse dato = new DatosDtoResponse();
			
			//datos personales 
			String nombre = c.getPersona().getNombre() +" "+c.getPersona().getPaterno()+" "+c.getPersona().getMaterno();
			dato.setIdPersona(c.getPersona().getId());
			dato.setIdp(c.getPersona().getIdp());
			dato.setNombre(nombre);
			dato.setEmail(c.getPersona().getEmail());
			//bancos
			dato.setBancoNombre(c.getBanco().getNombre());
			dato.setIdBanco(c.getBanco().getId());
			dato.setEstadoBanco(c.getBanco().getEstado());
			
			//cuenta  bancaria
			dato.setIdCuentaBancaria(c.getId());
			dato.setNumero(c.getNumero());
			dato.setTipoCuenta(c.getTipoCuenta());
			dato.setEstadoCuentaBancaria(c.getEstado());
			
			datos.add(dato);
			
		});
		
		
		datos.forEach(d->{
			empleados.forEach(e->{
				if(d.getIdPersona().longValue() == e.getPersona().getId().longValue()) {
					d.setIdpJefe(e.getIdpJefe());
					d.setTipoEmpleado(e.getTipo());
					//Sucursal
					d.setIdSucursal(e.getSucursal().getId());
					d.setCodigoSucursal(e.getSucursal().getCodigo());
					d.setNombreSucursal(e.getSucursal().getNombre());
					d.setPrefijoSucursal(e.getSucursal().getPrefijo());
					
					//Cargo
					d.setIdCargo(e.getCargo().getId());
					d.setCargoNombre(e.getCargo().getNombre());
					d.setEstadoCargo(e.getCargo().getEstado());
					
				}
				
			});
			
		});
		
		datos.forEach(d->{
			sac.forEach(s->{
				if(d.getIdSucursal().longValue() == s.getSucursal().getId().longValue()) {
					//Centro de Costos
					d.setIdCentroCostos(s.getCentrocostos().getId());
					d.setCodigoCentroCostos(s.getCentrocostos().getCodigo());
					d.setNombreCentroCostos(s.getCentrocostos().getNombre());
					d.setEstadoCentroCostos(s.getCentrocostos().getEstado());
					d.setPrefijoCentroCostos(s.getCentrocostos().getPrefijo());
					
					//Area
					d.setIdArea(s.getArea().getId());
					d.setNombreArea(s.getArea().getNombre());
					d.setEstadoArea(s.getArea().getEstado());
					
				}
			});
		});
		
		datos.forEach(d->{
			profesiones.forEach(p->{
				if(d.getIdPersona().longValue()==p.getPersona().getId().longValue()) {
					//Profesion
					
					d.setIdProfesion(p.getProfesion().getId());
					d.setProfesionNombre(p.getProfesion().getNombre());
				}
			});
		});
		
		datos.forEach(d->{
			personas.stream()
			.filter(p->d.getIdpJefe().equals(p.getIdp()))
			.findFirst()
			.ifPresent(p->{
				d.setIdJefe(p.getId());
				d.setNombreJefe(p.getNombre()+" "+p.getPaterno()+" "+p.getMaterno());
				
			});
		});
				
		return datos;
	}
	
	@Transactional
	public void setAllEstado(boolean estado) {
		
		this.empleadoService.setAllEstado(estado);
		this.usuarioService.setAllEstado(estado);
	}

}
