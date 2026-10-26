package com.saims.sgrf.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.sgrf.dto.AreaDto;
import com.saims.sgrf.dto.BancoDto;
import com.saims.sgrf.dto.CargoDto;
import com.saims.sgrf.dto.DatosDto;
import com.saims.sgrf.dto.DepartamentoDto;
import com.saims.sgrf.dto.EmpleadoDto;
import com.saims.sgrf.dto.PersonaDto;
import com.saims.sgrf.dto.ProfesionDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.enums.TipoEmpleado;
import com.saims.sgrf.model.AreaModel;
import com.saims.sgrf.model.BancoModel;
import com.saims.sgrf.model.CargoModel;
import com.saims.sgrf.model.CuentaBancariaModel;
import com.saims.sgrf.model.EmpleadoAreaModel;
import com.saims.sgrf.model.EmpleadoCargoModel;
import com.saims.sgrf.model.EmpleadoModel;
import com.saims.sgrf.model.NivelModel;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.model.PersonaProfesionModel;
import com.saims.sgrf.model.ProfesionModel;
import com.saims.sgrf.model.UsuarioModel;
import com.saims.sgrf.service.AreaService;
import com.saims.sgrf.service.BancoService;
import com.saims.sgrf.service.CargoService;
import com.saims.sgrf.service.CuentaBancariaService;
import com.saims.sgrf.service.DatosService;
import com.saims.sgrf.service.DepartamentoService;
import com.saims.sgrf.service.EmpleadoAreaService;
import com.saims.sgrf.service.EmpleadoCargoService;
import com.saims.sgrf.service.EmpleadoService;
import com.saims.sgrf.service.NivelService;
import com.saims.sgrf.service.PersonaProfesionService;
import com.saims.sgrf.service.PersonaService;
import com.saims.sgrf.service.ProfesionService;
import com.saims.sgrf.service.UsuarioService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DatosServiceImpl implements DatosService {

	private final ModelMapper modelMapper;

	private final PersonaService personaService;
	private final BancoService bancoService;
	private final CuentaBancariaService cuentaBancariaService;
	private final CargoService cargoService;
	private final ProfesionService profesionService;
	private final AreaService areaServise;

	private final EmpleadoService empleadoService;
	private final EmpleadoCargoService empleadoCargoService;

	private final UsuarioService usuarioService;
	private final PersonaProfesionService personaProfesionService;

	private final EmpleadoAreaService empleadoAreaService;
	
	private final NivelService nivelService;
	private final DepartamentoService departamentoService;

	@Transactional
	public DatosDto.Response AddDatos(DatosDto.Request request) throws Exception {

		/**
		 * Registro para persona y usuario
		 */
		PersonaModel personaModel = this.personaService.createOrGet(request);
		this.usuarioService.createOrGet(personaModel);

		/**
		 * Registro de banco y Cuenta Bancaria se requiere el registro de la persona
		 */
		BancoModel bancoModel = this.bancoService.createOrGet(request);
		this.cuentaBancariaService.createOrGet(personaModel, bancoModel, request);

		/**
		 * Registro o busqueda de profesion y agregasion de la profesion a la persona
		 */
		ProfesionModel profesionModel = this.profesionService.createOrGet(request);
		this.personaProfesionService.createOrGet(personaModel, profesionModel);

		/**
		 * registro o busqueda de Cargos
		 */
		CargoModel cargoModel = this.cargoService.createOrGet(request);

		/**
		 * registro de emplado y agregacion del cargo
		 */
		EmpleadoModel empleadoModel = this.empleadoService.addDatos(personaModel, request);
		this.empleadoCargoService.addEmpleadoCargo(empleadoModel, cargoModel, request);
		
		/**
		 * registro de Areas Centro de Costo y Subsentros de Costo
		 */
		AreaModel areaModel = this.areaServise.createOrGet(request);
		this.empleadoAreaService.createOrGet(empleadoModel, areaModel);

		return null;
	}

	@Transactional
	public List<DatosDto.Request> getEmpleadosFile() {
		List<UsuarioModel> usuarios = this.usuarioService.findAllModel(Estado.TODOS);

		List<DatosDto.Request> lista = new ArrayList<DatosDto.Request>();

		DatosDto.Request dato;
		PersonaProfesionModel profesion;
		EmpleadoDto.Response empleado;
		EmpleadoCargoModel cargo;
		CuentaBancariaModel banco;
		EmpleadoAreaModel empleadoArea;

		// a apartir de la lista de Usuarios creamos la lista de las personas
		boolean esPrimero = true;
		for (UsuarioModel value : usuarios) {
			if (esPrimero) {
				esPrimero = false;
				continue;
			}
			// recopilamos los datos del empleado en dato
			dato = new DatosDto.Request();

			// traemos los datos personales
			dato.setIdp(value.getPersona().getIdp());
			dato.setNombre(value.getPersona().getNombre());
			dato.setPaterno(value.getPersona().getPaterno());
			dato.setMaterno(value.getPersona().getMaterno());
			dato.setEmail(value.getPersona().getEmail());
			dato.setFoto(value.getPersona().getFoto());

			// traemos los datos de la profesion de la persona
			profesion = this.personaProfesionService.getPersonaProfesionActivo(value.getPersona().getId());
			dato.setProfesionNombre(profesion.getProfesion().getNombre());

			// Traemos los datos de la persona que es empleado reuiere el Id de la Persona y
			// el Id del Empleado
			empleado = this.empleadoService.findByPersonaId(value.getPersona().getId());
			cargo = this.empleadoCargoService.getCargoActivo(empleado.getId());
			dato.setCargoNombre(cargo.getCargo().getNombre());
			dato.setIdpJefe(cargo.getJefe().getIdp());

			// Treamos los datos de la Cuenta bancaria activa de la persona requiere el ID
			// de la Persona
			banco = this.cuentaBancariaService.getCuentaBancariaActivo(value.getPersona().getId());
			dato.setNumero(banco.getNumero());
			dato.setTipoCuenta(banco.getTipoCuenta());
			dato.setBancoNombre(banco.getBanco().getNombre());

			// Traemos los datos del Area de Trabajo de la Persona
			empleadoArea = this.empleadoAreaService.getEmpleado(empleado.getId());
			if (empleadoArea.getId().longValue() > 0L) {
				dato.setNombreCentroCostos(empleadoArea.getArea().getDepartamento().getNombre());
				dato.setCodigoCentroCostos(empleadoArea.getArea().getCodigo());
			}

			dato.setTipo(false);
			if (empleado.getTipo().equals(TipoEmpleado.JEFE))
				dato.setTipo(true);

			lista.add(dato);
		}
		return lista;
	}

	@Transactional
	public void setAllEstado(boolean estado) {

		this.empleadoService.setAllEstado(estado);
		this.usuarioService.setAllEstado(estado);
	}

	@Transactional
	public DatosDto.Response getDatos(Long idPersona) {
		DatosDto.Response datos = new DatosDto.Response();

		// buscamos los datos de la profesion de la persona
		PersonaProfesionModel personaProfesion = this.personaProfesionService.getPersonaProfesionActivo(idPersona);
		EmpleadoCargoModel empleadoCargo = null;
		
		// buscamos los datos del empleado y su cargo
		EmpleadoDto.Response empleado = this.empleadoService.findByPersonaId(idPersona);
		if (empleado.getId().longValue() > 0L) {
			// si el empleado existe buscamos su cargo activo
			empleadoCargo = this.empleadoCargoService.getCargoActivo(empleado.getId());
		}

		// buscamos la cuanta bancaria Activa del usuario
		CuentaBancariaModel cuenta = this.cuentaBancariaService.getCuentaBancariaActivo(idPersona);

		// Creamos los datos de Respuesta
		datos.setIdUsuario(0l);
		datos.setImagen("");

		// datos de la persona
		datos.setIdPersona(personaProfesion.getPersona().getId());
		datos.setIdp(personaProfesion.getPersona().getIdp());
		datos.setNombre(personaProfesion.getPersona().getNombre());
		datos.setApellido(
				personaProfesion.getPersona().getPaterno() + " " + personaProfesion.getPersona().getMaterno());
		datos.setEmail(personaProfesion.getPersona().getEmail());
		datos.setCelular(personaProfesion.getPersona().getCelular());

		// Datos para la profesion
		if (personaProfesion.getId().longValue() > 0L) {
			datos.setIdProfesion(personaProfesion.getProfesion().getId());
			datos.setProfesionNombre(personaProfesion.getProfesion().getNombre());
		}

		// Datos para el empleado
		if (empleado.getId().longValue() > 0L) {
			
			datos.setTipoEmpleado(empleadoCargo.getEmpleado().getTipo());
			datos.setEmailEmpleado(empleadoCargo.getEmpleado().getEmail());

			// Datos para el cargo del empleado
			datos.setIdCargo(empleadoCargo.getId());
			datos.setCargoNombre(empleadoCargo.getCargo().getNombre());
			datos.setEstadoCargo(empleadoCargo.getCargo().getEstado());

			// Datos del Jefe
			datos.setIdpJefe(empleadoCargo.getJefe().getIdp());
			datos.setIdJefe(empleadoCargo.getJefe().getId());
			datos.setNombreJefe(empleadoCargo.getJefe().getNombre() + " " + empleadoCargo.getJefe().getPaterno() + " "
					+ empleadoCargo.getJefe().getMaterno());

			// Datos del area de trabajo
			EmpleadoAreaModel empleadoArea = this.empleadoAreaService.getEmpleado(empleado.getId());
			datos.setIdArea(empleadoArea.getArea().getId());
			datos.setNombreArea(empleadoArea.getArea().getDepartamento().getNombre());
			datos.setCodigoArea(empleadoArea.getArea().getCodigo());
			datos.setPrefijoArea(empleadoArea.getArea().getPrefijo());
			
			NivelModel nivel = this.nivelService.findByIdHijo(empleadoArea.getArea());
			
		}

		return datos;
	}
	
	@Transactional
	public DatosDto.Perfil getPerfil(Long idPersona) {
		DatosDto.Perfil perfil = new DatosDto.Perfil();

		// buscamos los datos de la profesion de la persona
		PersonaProfesionModel personaProfesion = this.personaProfesionService.getPersonaProfesionActivo(idPersona);
		EmpleadoCargoModel empleadoCargo = null;
		
		// buscamos los datos del empleado y su cargo
		EmpleadoDto.Response empleado = this.empleadoService.findByPersonaId(idPersona);
		if (empleado.getId().longValue() > 0L) {
			// si el empleado existe buscamos su cargo activo
			empleadoCargo = this.empleadoCargoService.getCargoActivo(empleado.getId());
		}
		
		// Creamos los datos de Respuesta
		perfil.setIdPersona(personaProfesion.getPersona().getId());
		perfil.setIdp(personaProfesion.getPersona().getIdp());
		String nombre = personaProfesion.getPersona().getNombre()+" "+personaProfesion.getPersona().getPaterno()+" "+personaProfesion.getPersona().getMaterno();
		perfil.setNombre(nombre);
		perfil.setEmail(personaProfesion.getPersona().getEmail());
		perfil.setCelular(personaProfesion.getPersona().getCelular());
		
		perfil.setProfesion(personaProfesion.getProfesion().getNombre());

		// Datos para el empleado
		if (empleado.getId().longValue() > 0L) {
			perfil.setCargo(empleadoCargo.getCargo().getNombre());
			perfil.setCorporativo(empleadoCargo.getEmpleado().getEmail());

			// Datos del Jefe
			perfil.setIdpJefe(empleadoCargo.getJefe().getIdp());
			perfil.setJefe(empleadoCargo.getJefe().getNombre() + " " + empleadoCargo.getJefe().getPaterno() + " "
					+ empleadoCargo.getJefe().getMaterno());

			// Datos del area de trabajo
			EmpleadoAreaModel empleadoArea = this.empleadoAreaService.getEmpleado(empleado.getId());
			
			perfil.setSubCentroCostos(empleadoArea.getArea().getDepartamento().getNombre());
			perfil.setCodigoSubCentro(empleadoArea.getArea().getCodigo());
			
			NivelModel nivel = this.nivelService.findByIdHijo(empleadoArea.getArea());
			if(nivel.getId().longValue()>0L)
				perfil.setCentroCostos(nivel.getPadre().getDepartamento().getNombre());
			else
				perfil.setCentroCostos(perfil.getSubCentroCostos());
			
		}

		return perfil;
	}

	@Transactional
	public DatosDto.Response getDatosUsuario(Long idUsuario) {
		// creamos la respuesta para el usuario
		DatosDto.Response datos = new DatosDto.Response();

		// Buscamos al Usuario
		UsuarioModel usuario = this.usuarioService.getUsuarioModel(idUsuario);
		EmpleadoCargoModel empleadoCargo = null;

		// Buscamos la Profecion activa del Usuario
		PersonaProfesionModel personaProfesion = this.personaProfesionService
				.getPersonaProfesionActivo(usuario.getPersona().getId());

		// Buscamos los datos Empleados del Usuario
		EmpleadoDto.Response empleado = this.empleadoService.findByPersonaId(usuario.getPersona().getId());
		if (empleado.getId().longValue() > 0L) {
			// si el empledo existe buscamos su cargo Actctivo
			empleadoCargo = this.empleadoCargoService.getCargoActivo(empleado.getId());
		}

		// Buscamos la cuenta Bancaria Activa del Usuario
		CuentaBancariaModel cuenta = this.cuentaBancariaService.getCuentaBancariaActivo(usuario.getPersona().getId());

		// Creamos los datos de Respuesta
		datos.setIdUsuario(idUsuario);
		datos.setImagen(usuario.getPersona().getFoto());
		datos.setEstado(usuario.getEstado());
		datos.setPernicions(usuario.getPernicions());
		datos.setRol(usuario.getRol());

		// datos de la persona
		datos.setIdPersona(usuario.getPersona().getId());
		datos.setIdp(usuario.getPersona().getIdp());
		datos.setNombre(usuario.getPersona().getNombre());
		datos.setApellido(usuario.getPersona().getPaterno() + " " + usuario.getPersona().getMaterno());
		datos.setEmail(usuario.getPersona().getEmail());
		datos.setCelular(usuario.getPersona().getCelular());

		// Datos para la profesion
		if (personaProfesion.getId().longValue() > 0L) {
			datos.setIdProfesion(personaProfesion.getProfesion().getId());
			datos.setProfesionNombre(personaProfesion.getProfesion().getNombre());
		}

		if (cuenta.getId().longValue() > 0L) {
			datos.setIdCuentaBancaria(cuenta.getId());
			datos.setNumero(cuenta.getNumero());
			datos.setTipoCuenta(cuenta.getTipoCuenta());
			datos.setEstadoCuentaBancaria(cuenta.getEstado());

			datos.setIdBanco(cuenta.getBanco().getId());
			datos.setBancoNombre(cuenta.getBanco().getNombre());
			datos.setEstadoBanco(cuenta.getBanco().getEstado());
		}

		// Datos para el empleado
		if (empleado.getId().longValue() > 0L) {
			datos.setTipoEmpleado(empleadoCargo.getEmpleado().getTipo());
			datos.setEmailEmpleado(empleadoCargo.getEmpleado().getEmail());

			// Datos para el cargo del empleado
			datos.setIdCargo(empleadoCargo.getId());
			datos.setCargoNombre(empleadoCargo.getCargo().getNombre());
			datos.setEstadoCargo(empleadoCargo.getCargo().getEstado());

			// Datos del Jefe
			datos.setIdpJefe(empleadoCargo.getJefe().getIdp());
			datos.setIdJefe(empleadoCargo.getJefe().getId());
			datos.setNombreJefe(empleadoCargo.getJefe().getNombre() + " " + empleadoCargo.getJefe().getPaterno() + " "
					+ empleadoCargo.getJefe().getMaterno());

			// Datos del area de trabajo
			EmpleadoAreaModel empleadoArea = this.empleadoAreaService.getEmpleado(empleado.getId());
			datos.setIdArea(empleadoArea.getArea().getId());
			datos.setNombreArea(empleadoArea.getArea().getDepartamento().getNombre());
			datos.setCodigoArea(empleadoArea.getArea().getCodigo());
			datos.setPrefijoArea(empleadoArea.getArea().getPrefijo());
		}

		return datos;
	}

	@Transactional
	public DatosDto.Empleado getDatosEmpleados() {
		DatosDto.Empleado datos = new DatosDto.Empleado();
		List<ProfesionDto.Response> profesion = this.profesionService.getAll();
		List<AreaDto.Estructura> estructura = this.areaServise.getEstructura();
		
		List<CargoDto.Response> cargo = this.cargoService.getAll();
		List<BancoDto.Response> banco = this.bancoService.getAll();

		List<EmpleadoModel> empleado = this.empleadoService.findByEstado(true);
		
		List<PersonaDto.Response> jefe = new ArrayList<PersonaDto.Response>();
		
		List<EmpleadoCargoModel> responsable = this.empleadoCargoService.findByEstado(true);
			
		List<EmpleadoCargoModel> unico = responsable.stream()
		        .collect(Collectors.toMap(
		                emp -> emp.getIdpPago(), 
		                emp -> emp,                    
		                (existente, reemplazo) -> existente
		        ))
		        .values()
		        .stream()
		        .collect(Collectors.toList());
		
		List<PersonaDto.Response> pago = new ArrayList<PersonaDto.Response>();
		for(EmpleadoCargoModel value:unico) {
			for(EmpleadoModel item: empleado) {
				if(value.getIdpPago().equals(item.getPersona().getIdp())) {
					pago.add(this.modelMapper.map(item.getPersona(), PersonaDto.Response.class));
				}
			}
		}

		for (EmpleadoModel e : empleado) {
			jefe.add(this.modelMapper.map(e.getPersona(), PersonaDto.Response.class));
		}

		datos.setProfesion(profesion);
		datos.setEstructura(estructura);
		datos.setCargo(cargo);
		datos.setBanco(banco);
		datos.setJefe(jefe);
		datos.setPago(pago);

		return datos;
	}

}
