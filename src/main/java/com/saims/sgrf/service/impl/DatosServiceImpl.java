package com.saims.sgrf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.sgrf.dto.AreaDto;
import com.saims.sgrf.dto.BancoDto;
import com.saims.sgrf.dto.CargoDto;
import com.saims.sgrf.dto.CentroCostosDto;
import com.saims.sgrf.dto.DatosDto;
import com.saims.sgrf.dto.EmpleadoDto;
import com.saims.sgrf.dto.PersonaDto;
import com.saims.sgrf.dto.ProfesionDto;
import com.saims.sgrf.dto.SucursalDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.enums.TipoEmpleado;
import com.saims.sgrf.model.AreaModel;
import com.saims.sgrf.model.BancoModel;
import com.saims.sgrf.model.CargoModel;
import com.saims.sgrf.model.CentroCostosModel;
import com.saims.sgrf.model.CuentaBancariaModel;
import com.saims.sgrf.model.EmpleadoCargoModel;
import com.saims.sgrf.model.EmpleadoModel;
import com.saims.sgrf.model.EmpleadoSucursalModel;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.model.PersonaProfesionModel;
import com.saims.sgrf.model.ProfesionModel;
import com.saims.sgrf.model.SucursalModel;
import com.saims.sgrf.model.UsuarioModel;
import com.saims.sgrf.service.AreaService;
import com.saims.sgrf.service.BancoService;
import com.saims.sgrf.service.CargoService;
import com.saims.sgrf.service.CentroCostosService;
import com.saims.sgrf.service.CuentaBancariaService;
import com.saims.sgrf.service.DatosService;
import com.saims.sgrf.service.EmpleadoCargoService;
import com.saims.sgrf.service.EmpleadoService;
import com.saims.sgrf.service.EmpleadoSucursalService;
import com.saims.sgrf.service.PersonaProfesionService;
import com.saims.sgrf.service.PersonaService;
import com.saims.sgrf.service.ProfesionService;
import com.saims.sgrf.service.SucursalService;
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
	private final CentroCostosService centroCostosService;
	private final SucursalService sucursalService;

	private final EmpleadoService empleadoService;
	private final EmpleadoCargoService empleadoCargoService;
	private final EmpleadoSucursalService empleadoSucursalService;

	private final UsuarioService usuarioService;
	private final PersonaProfesionService personaProfesionService;

	@Transactional
	public DatosDto.Response AddDatos(DatosDto.Request request) throws Exception {

		/**
		 * Registro para persona
		 */
		PersonaModel personaModel = this.personaService.createOrGet(request);
		this.usuarioService.createOrGet(personaModel);

		// System.out.println("Datos Creados "+personaModel.getNombre());

		/**
		 * Registro para banco
		 */

		// System.out.println("Creando Banaco DatosService");
		BancoModel bancoModel = this.bancoService.createOrGet(request);
		// System.out.println("DAtos Creados =========="+bancoModel.getNombre());

		/**
		 * Registro de cuenta Bancaria
		 */
		// System.out.println("Creando Cuenta Bancaria DatosService");
		CuentaBancariaModel cuentaBancariaModel = this.cuentaBancariaService.createOrGet(personaModel, bancoModel,
				request);
		// System.out.println("Datos creados============"+
		// cuentaBancariaModel.getNumero());

		System.out.println("Creando Profesion DatosService");
		ProfesionModel profesionModel = this.profesionService.createOrGet(request);
		PersonaProfesionModel personaProfesionModel = this.personaProfesionService.createOrGet(personaModel,
				profesionModel);
		System.out.println("Datos Creados ============" + profesionModel.getNombre());

		/**
		 * registro de emplado de
		 */
		System.out.println("==================Creando Empleado DatosService");
		EmpleadoModel empleadoModel = this.empleadoService.addDatos(personaModel, request);

		/**
		 * registro para cargo
		 */
		System.out.println("Creando Cargo DatosService");
		CargoModel cargoModel = this.cargoService.createOrGet(request);

		// Registro de relacion empleado y cargo
		EmpleadoCargoModel eCargo = this.empleadoCargoService.addEmpleadoCargo(empleadoModel, cargoModel,
				request);

		/**
		 * registro para aprofesion
		 */

		/**
		 * registro de Area
		 */
		System.out.println("==================Creando Area DatosService");
		AreaModel areaModel = this.areaServise.createOrGet(request);
		System.out.println("Datos Creados ============" + areaModel.getNombre());

		/**
		 * registro de Centro de costos verificamos si en el request el nombre es
		 * ninguno si es ninguno mandamos un centro de costos nulo caso contrario
		 * mandamos a la funcion addDatos
		 */
		System.out.println("==================Creando Centro de Costos DatosService");
		// CentroCostosModel centroCostosModel = null;
		// if(!datosDtoRequest.getNombreCentroCostos().equals("Ninguno"))
		CentroCostosModel centroCostosModel = this.centroCostosService.createOrGet(request);
		System.out.println("Datos Creados ==================" + centroCostosModel.getNombre());
		/**
		 * registro de Sucursal
		 */

		System.out.println("==================Creando Sucursal DatosService");
		SucursalModel sucursalModel = this.sucursalService.createOrGet(request);
		System.out.println("Datos Creados ==================" + sucursalModel.getNombre());

		/**
		 * registro de relasion entre Sucursal y Area
		 */

		this.empleadoSucursalService.addEmpleadoSucursal(empleadoModel, sucursalModel, areaModel, centroCostosModel);

		return null;
	}

	@Transactional
	public List<DatosDto.Request> getEmpleadosFile() {

		List<UsuarioModel> usuarios = this.usuarioService.findAllModel(Estado.TODOS);
		List<DatosDto.Request> export = new ArrayList<DatosDto.Request>();
		DatosDto.Request dato;
		PersonaProfesionModel profesion;
		EmpleadoDto.Response empleado;
		EmpleadoCargoModel cargo;
		CuentaBancariaModel banco;
		EmpleadoSucursalModel sucursal;

		boolean esPrimero = true;
		for (UsuarioModel value : usuarios) {
			if (esPrimero) {
				esPrimero = false;
				continue;
			}
			dato = new DatosDto.Request();
			dato.setIdp(value.getPersona().getIdp());
			dato.setNombre(value.getPersona().getNombre());
			dato.setPaterno(value.getPersona().getPaterno());
			dato.setMaterno(value.getPersona().getMaterno());
			dato.setEmail(value.getPersona().getEmail());

			profesion = this.personaProfesionService.getPersonaProfesionActivo(value.getPersona().getId());
			dato.setProfesionNombre(profesion.getProfesion().getNombre());

			empleado = this.empleadoService.findByPersonaId(value.getPersona().getId());
			cargo = this.empleadoCargoService.getCargoActivo(empleado.getId());

			dato.setCargoNombre(cargo.getCargo().getNombre());

			banco = this.cuentaBancariaService.getCuentaBancariaActivo(value.getPersona().getId());
			dato.setNumero(banco.getNumero());
			dato.setTipoCuenta(banco.getTipoCuenta());
			dato.setBancoNombre(banco.getBanco().getNombre());

			sucursal = this.empleadoSucursalService.getSucursalActivo(empleado.getId());
			dato.setCodigoSucursal(sucursal.getSucursal().getCodigo());
			dato.setNombreSucursal(sucursal.getSucursal().getNombre());
			dato.setCodigoCentroCostos(sucursal.getCentrocostos().getCodigo());
			dato.setNombreCentroCostos(sucursal.getCentrocostos().getNombre());
			dato.setNombreArea(sucursal.getArea().getNombre());
			dato.setIdpJefe(cargo.getJefe().getIdp());
			dato.setTipo(false);
			if (empleado.getTipo().equals(TipoEmpleado.JEFE))
				dato.setTipo(true);
			export.add(dato);
		}
		return export;
	}

	@Transactional
	public void setAllEstado(boolean estado) {

		this.empleadoService.setAllEstado(estado);
		this.usuarioService.setAllEstado(estado);
	}

	@Transactional
	public DatosDto.Response getDatos(Long idPersona) {
		DatosDto.Response datos = new DatosDto.Response();

		PersonaProfesionModel personaProfesion = this.personaProfesionService.getPersonaProfesionActivo(idPersona);
		EmpleadoDto.Response empleado = this.empleadoService.findByPersonaId(idPersona);

		EmpleadoCargoModel empleadoCargo = this.empleadoCargoService.getCargoActivo(empleado.getId());

		EmpleadoSucursalModel sucursal = this.empleadoSucursalService.getSucursalActivo(empleado.getId());
		CuentaBancariaModel cuenta = this.cuentaBancariaService.getCuentaBancariaActivo(idPersona);
		// EmpleadoDtoResponse empleado =
		// this.empleadoService.findByPersonaId(idPersona);

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
		datos.setIdProfesion(personaProfesion.getProfesion().getId());
		datos.setProfesionNombre(personaProfesion.getProfesion().getNombre());

		datos.setIdCuentaBancaria(cuenta.getId());
		datos.setNumero(cuenta.getNumero());
		datos.setTipoCuenta(cuenta.getTipoCuenta());
		datos.setEstadoCuentaBancaria(cuenta.getEstado());

		datos.setIdBanco(cuenta.getBanco().getId());
		datos.setBancoNombre(cuenta.getBanco().getNombre());
		datos.setEstadoBanco(cuenta.getBanco().getEstado());

		// Datos para el empleado
		datos.setTipoEmpleado(empleadoCargo.getEmpleado().getTipo());

		// Datos para el cargo del empleado
		datos.setIdCargo(empleadoCargo.getId());
		datos.setCargoNombre(empleadoCargo.getCargo().getNombre());
		datos.setEstadoCargo(empleadoCargo.getCargo().getEstado());

		datos.setIdpJefe(empleadoCargo.getJefe().getNombre());
		datos.setIdJefe(1l);
		datos.setNombreJefe("Buscar Nombre");

		// Datos para sucursal
		datos.setIdSucursal(sucursal.getSucursal().getId());
		datos.setCodigoSucursal(sucursal.getSucursal().getCodigo());
		datos.setNombreSucursal(sucursal.getSucursal().getNombre());
		datos.setPrefijoSucursal(sucursal.getSucursal().getPrefijo());

		// Datos para Centro de Costos
		datos.setIdCentroCostos(sucursal.getCentrocostos().getId());
		datos.setCodigoCentroCostos(sucursal.getCentrocostos().getCodigo());
		datos.setNombreCentroCostos(sucursal.getCentrocostos().getNombre());
		datos.setPrefijoCentroCostos(sucursal.getCentrocostos().getPrefijo());
		datos.setEstadoCentroCostos(sucursal.getCentrocostos().getEstado());

		// Datos para Area
		datos.setIdArea(sucursal.getArea().getId());
		datos.setNombreArea(sucursal.getArea().getNombre());
		datos.setEstadoArea(sucursal.getArea().getEstado());

		return datos;
	}

	@Transactional
	public DatosDto.Response getDatosUsuario(Long idUsuario) {
		DatosDto.Response datos = new DatosDto.Response();
		UsuarioModel usuario = this.usuarioService.getUsuarioModel(idUsuario);
		EmpleadoCargoModel empleadoCargo = null;
		EmpleadoSucursalModel sucursal = null;

		PersonaProfesionModel personaProfesion = this.personaProfesionService
				.getPersonaProfesionActivo(usuario.getPersona().getId());

		EmpleadoDto.Response empleado = this.empleadoService.findByPersonaId(usuario.getPersona().getId());
		if (empleado.getId().longValue() > 0L) {
			empleadoCargo = this.empleadoCargoService.getCargoActivo(empleado.getId());
			sucursal = this.empleadoSucursalService.getSucursalActivo(empleado.getId());
		}

		CuentaBancariaModel cuenta = this.cuentaBancariaService.getCuentaBancariaActivo(usuario.getPersona().getId());
		// EmpleadoDtoResponse empleado =
		// this.empleadoService.findByPersonaId(idPersona);

		// datos usuario
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

		if(cuenta.getId().longValue()>0L) {
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

			// Datos para el cargo del empleado
			datos.setIdCargo(empleadoCargo.getId());
			datos.setCargoNombre(empleadoCargo.getCargo().getNombre());
			datos.setEstadoCargo(empleadoCargo.getCargo().getEstado());

			datos.setIdpJefe(empleadoCargo.getJefe().getIdp());
			datos.setIdJefe(empleadoCargo.getJefe().getId());
			datos.setNombreJefe(empleadoCargo.getJefe().getNombre() + " " + empleadoCargo.getJefe().getPaterno() + " "
					+ empleadoCargo.getJefe().getMaterno());

			// Datos para sucursal
			datos.setIdSucursal(sucursal.getSucursal().getId());
			datos.setCodigoSucursal(sucursal.getSucursal().getCodigo());
			datos.setNombreSucursal(sucursal.getSucursal().getNombre());
			datos.setPrefijoSucursal(sucursal.getSucursal().getPrefijo());

			// Datos para Centro de Costos
			datos.setIdCentroCostos(sucursal.getCentrocostos().getId());
			datos.setCodigoCentroCostos(sucursal.getCentrocostos().getCodigo());
			datos.setNombreCentroCostos(sucursal.getCentrocostos().getNombre());
			datos.setPrefijoCentroCostos(sucursal.getCentrocostos().getPrefijo());
			datos.setEstadoCentroCostos(sucursal.getCentrocostos().getEstado());

			// Datos para Area
			datos.setIdArea(sucursal.getArea().getId());
			datos.setNombreArea(sucursal.getArea().getNombre());
			datos.setEstadoArea(sucursal.getArea().getEstado());
		}

		return datos;
	}

	@Transactional
	public DatosDto.Empleado getDatosEmpleados() {
		DatosDto.Empleado datos = new DatosDto.Empleado();

		List<ProfesionDto.Response> profesion = this.profesionService.getAll();
		List<SucursalDto.Response> sucursal = this.sucursalService.findAll();
		List<AreaDto.Response> area = this.areaServise.findByAll(Estado.TODOS);
		List<CentroCostosDto.Response> centro = this.centroCostosService.findAll();
		List<CargoDto.Response> cargo = this.cargoService.getAll();
		List<BancoDto.Response> banco = this.bancoService.getAll();

		List<EmpleadoModel> empleado = this.empleadoService.findByTipo(TipoEmpleado.JEFE);
		List<PersonaDto.Response> jefe = new ArrayList<PersonaDto.Response>();

		for (EmpleadoModel e : empleado) {
			jefe.add(this.modelMapper.map(e.getPersona(), PersonaDto.Response.class));
		}

		datos.setProfesion(profesion);
		datos.setSucursal(sucursal);
		datos.setArea(area);
		datos.setCentrocostos(centro);
		datos.setCargo(cargo);
		datos.setBanco(banco);
		datos.setJefe(jefe);

		return datos;
	}

}
