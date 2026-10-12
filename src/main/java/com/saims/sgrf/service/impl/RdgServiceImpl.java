package com.saims.sgrf.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.saims.sgrf.config.EmailTemplate;
import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.RdgDao;
import com.saims.sgrf.dao.SrfDao;
import com.saims.sgrf.dto.AprobacionDto;
import com.saims.sgrf.dto.CuentaDto;
import com.saims.sgrf.dto.RdgDetalleDto;
import com.saims.sgrf.dto.RdgDevolverDto;
import com.saims.sgrf.dto.RdgDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.CuentaBancariaModel;
import com.saims.sgrf.model.CuentaModel;
import com.saims.sgrf.model.EmpleadoAreaModel;
import com.saims.sgrf.model.EmpleadoCargoModel;
import com.saims.sgrf.model.EmpleadoModel;
import com.saims.sgrf.model.NivelModel;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.model.RdgAprobacionModel;
import com.saims.sgrf.model.RdgModel;
import com.saims.sgrf.model.SettingsModel;
import com.saims.sgrf.model.SrfModel;
import com.saims.sgrf.service.AprobacionService;
import com.saims.sgrf.service.CuentaBancariaService;
import com.saims.sgrf.service.CuentaService;
import com.saims.sgrf.service.EmailService;
import com.saims.sgrf.service.EmpleadoAreaService;
import com.saims.sgrf.service.EmpleadoCargoService;
import com.saims.sgrf.service.EmpleadoService;
import com.saims.sgrf.service.NivelService;
import com.saims.sgrf.service.PersonaService;
import com.saims.sgrf.service.RdgDetalleService;
import com.saims.sgrf.service.RdgDevolverService;
import com.saims.sgrf.service.RdgService;
import com.saims.sgrf.service.SettingsService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RdgServiceImpl implements RdgService {

	private final RdgDao rdgDao;
	private final SrfDao srfDao;
	private Personalizer personalizer = new Personalizer();
	private final ModelMapper modelMapper;
	private final EmpleadoService empleadoService;
	private final CuentaBancariaService cuentaBancariaService;
	private final EmpleadoCargoService empleadoCargoService;
	private final EmpleadoAreaService empleadoAreaService;
	private final NivelService nivelService;
	private final CuentaService cuentaService;
	private final RdgDetalleService rdgDetalleService;
	private final RdgDevolverService rdgDevolverService;
	private final PersonaService personaService;
	private final AprobacionService.Rdg aprobacionRdgService;
	private final SettingsService settingsService;
	private final EmailService emailService;

	@Transactional
	public RdgModel createOrGet(SrfModel srf, EmpleadoModel empleado) {

		RdgModel rdg = this.rdgDao.findBySrf(srf.getId()).orElseGet(() -> {

			int n = this.rdgDao.countNumero(empleado.getId(), srf.getGestion());
			RdgModel nuevo = new RdgModel();
			nuevo.setNumero(this.personalizer.getNunero(n));
			nuevo.setGestion(srf.getGestion());
			nuevo.setMoneda(srf.getMoneda());
			nuevo.setEmpleado(empleado);
			nuevo.setIdSrf(srf.getId());
			nuevo.setImporte(srf.getMonto());
			nuevo.setActividad(srf.getDetalle());
			nuevo.setEstado(Estado.PENDIENTE);
			this.rdgDao.save(nuevo);
			return nuevo;
		});
		return rdg;
	}

	@Transactional
	public RdgModel createTramite(SrfModel srf, EmpleadoModel empleado) {
		String token = UUID.randomUUID().toString();

		RdgModel rdg = this.rdgDao.findBySrf(srf.getId()).orElseGet(() -> {

			int n = this.rdgDao.countNumero(empleado.getId(), srf.getGestion());
			RdgModel nuevo = new RdgModel();
			nuevo.setNumero(this.personalizer.getNunero(n));
			nuevo.setGestion(srf.getGestion());
			nuevo.setMoneda(srf.getMoneda());
			nuevo.setEmpleado(empleado);
			nuevo.setIdSrf(srf.getId());
			nuevo.setImporte(srf.getMonto());
			nuevo.setActividad(srf.getDetalle());
			nuevo.setFechaSolicitud(srf.getFechaSolicitud());
			nuevo.setDevolver(BigDecimal.ZERO);
			nuevo.setGasto(BigDecimal.ZERO);
			nuevo.setRembolso(BigDecimal.ZERO);
			nuevo.setSaldo(BigDecimal.ZERO);
			nuevo.setEstado(Estado.TRAMITE);
			nuevo.setToken(token);
			this.rdgDao.save(nuevo);
			return nuevo;
		});
		return rdg;
	}

	@Transactional
	public RdgModel createRdg(RdgDto.Request request) {
		/*
		 * Se crea el formulario de Redncicion de gastos sin un formulario de Solicitud
		 */
		String token = UUID.randomUUID().toString();
		int gestion = java.time.LocalDate.now().getYear();

		List<CuentaModel> cuentas = this.cuentaService.findAll();

		// traemos todos los empleados activos y al empleado
		List<EmpleadoModel> empleados = this.empleadoService.findByEstado(true);
		EmpleadoModel empleado = this.getEmpleado(empleados, request.getIdPersona());

		// traemos todos los cargos activos
		List<EmpleadoCargoModel> empleadoCargos = this.empleadoCargoService.findByEstado(true);

		// creamos la lista de Jefes
		List<PersonaModel> listaJefes = new ArrayList<PersonaModel>();
		PersonaModel idPago = this.getPersonaPago(empleadoCargos, empleado.getId());
		this.getJefes(listaJefes, empleado.getId(), empleadoCargos, empleados);

		// Creamos el Formulario
		int n = this.rdgDao.countNumero(empleado.getId(), gestion);
		RdgModel rdg = new RdgModel();

		rdg.setNumero(this.personalizer.getNunero(n));
		rdg.setGestion(gestion);
		rdg.setEmpleado(empleado);
		rdg.setIdSrf(0L);

		rdg.setActividad(request.getActividad());
		rdg.setFechaRendicion(request.getFecha());
		rdg.setFechaSolicitud(request.getFecha());
		rdg.setAl(this.personalizer.getFecha(request.getAl()));
		rdg.setDel(this.personalizer.getFecha(request.getDel()));
		rdg.setDetalle(request.getDetalle());
		rdg.setArea(request.getArea());
		rdg.setCargo(request.getCargo());
		rdg.setCentroCostos(request.getCentrocostos());
		rdg.setCodigo(request.getCodigo());
		rdg.setEstado(Estado.PENDIENTE);
		rdg.setFormato(request.getFormato());
		rdg.setDevolver(request.getDevolver());
		rdg.setGasto(request.getGasto());
		rdg.setImporte(request.getImporte());
		rdg.setRembolso(request.getRembolso());
		rdg.setSaldo(request.getSaldo());
		rdg.setTipoCambio(request.getTc());
		rdg.setMoneda(request.getMoneda());
		rdg.setParticipantes(request.getParticipantes());
		rdg.setTipoCuenta(request.getTipoCuenta());
		rdg.setBanco(request.getBanco());
		rdg.setCuenta(request.getCuenta());
		rdg.setToken(token);

		this.rdgDao.save(rdg);

		// Creamos la lista de Aprobaciones y enviamos el Correo al primer Jefe
		List<Boolean> detalleBol = new ArrayList<Boolean>();
		this.createAprobacion(listaJefes, rdg, empleadoCargos, empleados, idPago, detalleBol);

		Map<String, Object> mapa = new HashMap<>();
		mapa.put("estado", detalleBol);

		// Creamos la lista de facturas
		this.createDetalle(request.getFactura(), cuentas, rdg, mapa);
		return rdg;
	}

	@Transactional
	public RdgModel createUpdateRdg(RdgDto.Response response) {

		EmpleadoModel empleado = this.empleadoService.findByPersonaIdModel(response.getIdPersona());

		BigDecimal devolverActual = BigDecimal.ZERO;
		List<CuentaModel> cuentas = this.cuentaService.findAll();

		RdgModel rdg = this.rdgDao.findByRdg(response.getIdRdg(), empleado.getId()).orElseGet(() -> {
			RdgModel nuevo = new RdgModel();
			nuevo.setId(0L);
			return nuevo;
		});

		if (rdg.getId().longValue() > 0L) {
			
			devolverActual = rdg.getDevolver();
			// traemos todos los empleados activos
			List<EmpleadoModel> empleados = this.empleadoService.findByEstado(true);
			// traemos todos los cargos activos
			List<EmpleadoCargoModel> empleadoCargos = this.empleadoCargoService.findByEstado(true);

			// creamos la lista de Jefes
			List<PersonaModel> listaJefes = new ArrayList<PersonaModel>();
			PersonaModel idPago = this.getPersonaPago(empleadoCargos, empleado.getId());
			this.getJefes(listaJefes, empleado.getId(), empleadoCargos, empleados);

			// Actualizamos los datos del Fomrulario
			rdg.setFechaRendicion(response.getFecha());
			rdg.setAl(this.personalizer.getFecha(response.getAl()));
			rdg.setDel(this.personalizer.getFecha(response.getDel()));
			rdg.setDetalle(response.getDetalle());

			rdg.setArea(response.getArea());
			rdg.setCargo(response.getCargo());
			rdg.setCentroCostos(response.getCentrocostos());
			rdg.setCodigo(response.getCodigo());
			rdg.setEstado(response.getEstado());
			rdg.setFormato(response.getFormato());
			rdg.setDevolver(response.getDevolver());
			rdg.setGasto(response.getGasto());
			rdg.setRembolso(response.getRembolso());
			rdg.setSaldo(response.getSaldo());
			rdg.setTipoCambio(response.getTc());
			rdg.setParticipantes(response.getParticipantes());
			rdg.setTipoCuenta(response.getTipoCuenta());
			rdg.setBanco(response.getBanco());
			rdg.setCuenta(response.getCuenta());

			this.rdgDao.save(rdg);
			List<Boolean> detalleBol = new ArrayList<Boolean>();
			// Creamos el Detalle de las Facturas y las Aprobaciones
			this.createAprobacion(listaJefes, rdg, empleadoCargos, empleados, idPago, detalleBol);

			// Creamos el mapa de estados
			Map<String, Object> mapa = new HashMap<>();
			mapa.put("estado", detalleBol);

			this.createDetalle(response.getFactura(), cuentas, rdg, mapa);

			// modificamos las Devoluciones si exsisten
			if (response.getDevolver().compareTo(BigDecimal.ZERO) > 0 || devolverActual.compareTo(BigDecimal.ZERO)>0) {
				this.createDevolver(response.getDeposito(), rdg);
			}
		}
		return rdg;
	}

	@Transactional
	public RdgModel saveRdg(RdgDto.Response response) {

		EmpleadoModel empleado = this.empleadoService.findByPersonaIdModel(response.getIdPersona());
		List<CuentaModel> cuentas = this.cuentaService.findAll();

		RdgModel rdg = this.rdgDao.findByRdg(response.getIdRdg(), empleado.getId()).orElseGet(() -> {
			RdgModel nuevo = new RdgModel();
			nuevo.setId(0L);
			return nuevo;
		});

		if (rdg.getId().longValue() > 0L) {

			rdg.setFechaRendicion(response.getFecha());
			rdg.setAl(this.personalizer.getFecha(response.getAl()));
			rdg.setDel(this.personalizer.getFecha(response.getDel()));
			rdg.setDetalle(response.getDetalle());

			rdg.setArea(response.getArea());
			rdg.setCargo(response.getCargo());
			rdg.setCentroCostos(response.getCentrocostos());
			rdg.setCodigo(response.getCodigo());
			rdg.setEstado(Estado.TRAMITE);
			rdg.setFormato(response.getFormato());
			rdg.setDevolver(response.getDevolver());
			rdg.setGasto(response.getGasto());
			rdg.setRembolso(response.getRembolso());
			rdg.setSaldo(response.getSaldo());
			rdg.setTipoCambio(response.getTc());
			rdg.setParticipantes(response.getParticipantes());
			rdg.setTipoCuenta(response.getTipoCuenta());
			rdg.setBanco(response.getBanco());
			rdg.setCuenta(response.getCuenta());

			this.rdgDao.save(rdg);

			Map<String, Object> mapa = new HashMap<>();
			List<Boolean> detalleBol = new ArrayList<Boolean>();
			detalleBol.add(false);
			mapa.put("estado", detalleBol);

			this.createDetalle(response.getFactura(), cuentas, rdg, mapa);
		}
		return rdg;
	}

	@Transactional
	public List<RdgDto.Srf> getRdgUsuarioEstado(Long idPersona, Estado estado) {

		EmpleadoModel empleado = this.empleadoService.findByPersonaIdModel(idPersona);

		List<RdgDto.Srf> lista = this.rdgDao.findByIdEmpleadoEstado(empleado.getId(), estado.name()).stream()
				.map(srf -> this.modelToSrf(srf)).collect(Collectors.toList());

		return lista;

	}

	public List<Integer> getRdgEstado(Long idPersona) {
		EmpleadoModel empleado = this.empleadoService.findByPersonaIdModel(idPersona);

		List<Integer> estado = new ArrayList<Integer>();

		if (empleado.getId().longValue() > 0L) {
			List<RdgModel> listaRdg = this.rdgDao.findByIdEstado(empleado.getId());
			List<RdgAprobacionModel> listaApr = this.aprobacionRdgService.findByIdPersona(idPersona);
			// Solicitudes

			estado.add((int) listaRdg.stream().filter(s -> s.getEstado() == Estado.TRAMITE).count());
			estado.add((int) listaRdg.stream().filter(s -> s.getEstado() == Estado.PENDIENTE).count());
			estado.add((int) listaRdg.stream().filter(s -> s.getEstado() == Estado.APROBADO).count());
			estado.add((int) listaRdg.stream().filter(s -> s.getEstado() == Estado.RECHAZADO).count());
			// Reviciones
			estado.add((int) listaApr.stream().filter(s -> s.getEstado() == Estado.PENDIENTE).count());
			estado.add((int) listaApr.stream().filter(s -> s.getEstado() == Estado.APROBADO).count());
			estado.add((int) listaApr.stream().filter(s -> s.getEstado() == Estado.RECHAZADO).count());
		} else {
			estado.add(0);
			estado.add(0);
			estado.add(0);
			estado.add(0);
			estado.add(0);
			estado.add(0);
			estado.add(0);

		}
		return estado;

	}

	@Transactional
	public List<RdgDto.Lista> getRdgLista(Long idPersona, Estado estado) {

		EmpleadoModel empleado = this.empleadoService.findByPersonaIdModel(idPersona);
		List<RdgModel> rdg = this.rdgDao.findByIdEmpleadoEstado(empleado.getId(), estado.name());

		List<RdgDto.Lista> lista = new ArrayList<RdgDto.Lista>();
		RdgDto.Lista model = null;
		for (RdgModel value : rdg) {
			model = new RdgDto.Lista();
			model.setId(value.getId());
			model.setDetalle(value.getDetalle());
			model.setNumero(value.getNumero());
			model.setGestion(value.getGestion());
			model.setImporte(value.getImporte());
			model.setRembolso(value.getRembolso());
			model.setSaldo(value.getSaldo());
			model.setDevolver(value.getDevolver());
			model.setGasto(value.getGasto());
			model.setMoneda(value.getMoneda());
			model.setEstado(value.getEstado());
			model.setFecha(value.getFechaRendicion());
			model.setToken(value.getToken());
			model.setActividad(value.getActividad());

			lista.add(model);
		}
		return lista;
	}

	@Transactional
	public RdgDto.Response getRdg(Long idRdg) {
		RdgModel rdg = this.rdgDao.findById(idRdg).orElseGet(() -> {
			RdgModel nuevo = new RdgModel();
			nuevo.setId(0L);
			nuevo.setEmpleado(null);
			return nuevo;
		});
		return this.modelToResponse(rdg);
	}

	@Transactional
	public RdgDto.CreateRdg getRdgUsuario(String idRdg, Long idPersona) {
		// se crea los datos para el formulario RDG con los datos del usuario si existe
		// el RDG

		List<CuentaDto.Response> cuentaGasto = this.cuentaService.getCuentas(Estado.GASTO);

		EmpleadoModel empleado = this.empleadoService.findByPersonaIdModel(idPersona);
		RdgModel rdg = this.rdgDao.findByTokenIdEmpleado(idRdg, empleado.getId()).orElseGet(() -> {
			RdgModel nuevo = new RdgModel();
			nuevo.setId(0L);
			nuevo.setEmpleado(null);
			return nuevo;
		});

		RdgDto.CreateRdg response = new RdgDto.CreateRdg();

		CuentaBancariaModel cuenta = this.cuentaBancariaService.getCuentaBancariaActivo(idPersona);

		EmpleadoCargoModel cargo = this.empleadoCargoService.getCargoActivo(empleado.getId());
		EmpleadoAreaModel area = this.empleadoAreaService.getEmpleado(cargo.getEmpleado().getId());
		NivelModel nivel = this.nivelService.findByIdHijo(area.getArea());

		if (rdg.getId().longValue() > 0L) {

			List<RdgDetalleDto.Response> factura = this.rdgDetalleService.findByIdRdgByAgregadoResponse(rdg.getId(),
					Estado.SI);

			SrfModel srf = this.srfDao.findById(rdg.getIdSrf()).orElseGet(() -> {
				SrfModel nuevo = new SrfModel();
				nuevo.setId(0L);
				return nuevo;
			});

			response.setNombre(cuenta.getPersona().getNombre() + " " + cuenta.getPersona().getPaterno() + " "
					+ cuenta.getPersona().getMaterno());
			response.setIdp(cuenta.getPersona().getIdp());
			response.setCargo(cargo.getCargo().getNombre());
			response.setTipo(cuenta.getTipoCuenta());
			response.setCuenta(cuenta.getNumero());
			response.setBanco(cuenta.getBanco().getNombre());
			response.setArea(nivel.getPadre().getDepartamento().getNombre());
			response.setCentrocostos(area.getArea().getDepartamento().getNombre());
			response.setCodigo(area.getArea().getCodigo());

			response.setActividad(rdg.getActividad());

			response.setDetalle(rdg.getDetalle());
			response.setDevolver(rdg.getDevolver());
			response.setEstado(rdg.getEstado());
			response.setFecha(rdg.getFechaRendicion());
			response.setFormato(rdg.getFormato());
			response.setGasto(rdg.getGasto());
			response.setGestion(rdg.getGestion());
			response.setIdSrf(rdg.getIdSrf());

			response.setIdRdg(rdg.getId());
			response.setImporte(rdg.getImporte());
			response.setMoneda(rdg.getMoneda());
			response.setNumero(rdg.getNumero());
			response.setParticipantes(rdg.getParticipantes());
			response.setRembolso(rdg.getRembolso());
			response.setSaldo(rdg.getSaldo());
			response.setTc(rdg.getTipoCambio());
			response.setFactura(factura);

			if (srf.getId().longValue() > 0L) {
				response.setNumeroSrf(srf.getNumero());
				response.setGestionSrf(srf.getGestion());
				if (rdg.getAl() == null) {
					response.setAl(srf.getFechaEstimada().toString().split("T")[0]);
					response.setDel(srf.getFechaSolicitud().split(" ")[0]);
				} else {
					response.setAl(rdg.getAl().toString().split("T")[0]);
					response.setDel(rdg.getDel().toString().split("T")[0]);
				}
			}

		} else {
			response.setIdRdg(rdg.getId());
			response.setNombre(cuenta.getPersona().getNombre() + " " + cuenta.getPersona().getPaterno() + " "
					+ cuenta.getPersona().getMaterno());
			response.setIdp(cuenta.getPersona().getIdp());
			response.setCargo(cargo.getCargo().getNombre());
			response.setTipo(cuenta.getTipoCuenta());
			response.setCuenta(cuenta.getNumero());
			response.setBanco(cuenta.getBanco().getNombre());
			response.setArea(nivel.getPadre().getDepartamento().getNombre());
			response.setCentrocostos(area.getArea().getDepartamento().getNombre());
			response.setCodigo(area.getArea().getCodigo());
		}
		response.setCuentaGasto(cuentaGasto);
		return response;
	}

	@Transactional
	public RdgDto.Solicitud updateRdgSolicitud(RdgDto.Solicitud response) {

		EmpleadoModel empleado = this.empleadoService.findByPersonaIdModel(response.getIdPersona());
		List<CuentaModel> cuentas = this.cuentaService.findAll();

		RdgModel rdg = this.rdgDao
				.getRdgSolicitud(response.getIdRdg(), response.getIdSrf(), empleado.getId(), response.getToken())
				.orElseGet(() -> {
					RdgModel nuevo = new RdgModel();
					nuevo.setId(0L);
					nuevo.setEmpleado(null);
					return nuevo;
				});

		if (rdg.getId().longValue() > 0L) {
			List<RdgAprobacionModel> aprobadores = this.aprobacionRdgService.findByIdRdgModel(rdg.getId());
			rdg.setActividad(response.getActividad());
			rdg.setMoneda(response.getMoneda());
			rdg.setDel(this.personalizer.getFecha(response.getDel()));
			rdg.setAl(this.personalizer.getFecha(response.getAl()));
			rdg.setParticipantes(response.getParticipantes());
			rdg.setDetalle(response.getDetalle());
			rdg.setDevolver(response.getDevolver());
			rdg.setGasto(response.getGasto());
			rdg.setRembolso(response.getRembolso());
			rdg.setSaldo(response.getSaldo());
			rdg.setEstado(response.getEstado());

			this.rdgDao.save(rdg);

			this.updateDetalle(response, cuentas, rdg);

			for (RdgAprobacionModel value : aprobadores) {
				if (value.getDestino() == 1) {
					value.setEstado(Estado.PENDIENTE);
					correoRdg(value, rdg, "MODIFICADO ");
				} else {
					value.setEstado(Estado.ESPERA);
				}
				this.aprobacionRdgService.createModel(value);
			}
		}
		return response;
	}

	@Transactional
	public RdgDto.Detalle getRdgDetalle(String idRdg, Long idPersona) {

		EmpleadoModel empleado = this.empleadoService.findByPersonaIdModel(idPersona);
		RdgDto.Detalle detalle = new RdgDto.Detalle();
		RdgModel rdgModel = this.rdgDao.findByTokenIdEmpleado(idRdg, empleado.getId()).orElseGet(() -> {
			RdgModel nuevo = new RdgModel();
			nuevo.setId(0L);
			return nuevo;
		});

		if (rdgModel.getId().longValue() > 0L) {
			List<CuentaModel> cuentas = this.cuentaService.findAll();
			List<CuentaDto.Resumen> resumen = new ArrayList<CuentaDto.Resumen>();

			detalle.setIdRdg(rdgModel.getId());
			detalle.setIdSrf(rdgModel.getIdSrf());
			detalle.setActividad(rdgModel.getActividad());
			detalle.setFecha(rdgModel.getFechaRendicion());
			detalle.setAl(rdgModel.getAl().toString());
			detalle.setDel(rdgModel.getDel().toString());
			detalle.setArea(rdgModel.getArea());
			detalle.setCargo(rdgModel.getCargo());
			detalle.setCentrocostos(rdgModel.getCentroCostos());
			detalle.setCodigo(rdgModel.getCodigo());
			detalle.setEstado(rdgModel.getEstado());
			detalle.setFormato(rdgModel.getFormato());
			detalle.setDevolver(rdgModel.getDevolver());
			detalle.setGasto(rdgModel.getGasto());
			detalle.setImporte(rdgModel.getImporte());
			detalle.setRembolso(rdgModel.getRembolso());
			detalle.setSaldo(rdgModel.getSaldo());
			detalle.setTc(rdgModel.getTipoCambio());
			detalle.setMoneda(rdgModel.getMoneda());
			detalle.setParticipantes(rdgModel.getParticipantes());
			detalle.setTipoCuenta(rdgModel.getTipoCuenta());
			detalle.setCuenta(rdgModel.getCuenta());
			detalle.setBanco(rdgModel.getBanco());
			detalle.setDetalle(rdgModel.getDetalle());
			detalle.setNumero(rdgModel.getNumero());
			detalle.setGestion(rdgModel.getGestion());
			detalle.setToken(rdgModel.getToken());

			List<RdgDetalleDto.Response> factura = this.rdgDetalleService
					.findByIdRdgByAgregadoResponse(rdgModel.getId(), Estado.SI);
			detalle.setFactura(factura);
			
			List<RdgDevolverDto.Response>devolver = this.rdgDevolverService.findByIdRdgByAgregadoResponse(rdgModel.getId(), Estado.SI);
			detalle.setDeposito(devolver);

			List<AprobacionDto.RdgResponse> aprobadores = this.aprobacionRdgService
					.findByIdRdgResponse(rdgModel.getId());

			detalle.setAprobadores(aprobadores);

			boolean pdf = aprobadores.stream().allMatch(a -> a.getEstado() == Estado.APROBADO);
			boolean editar = aprobadores.stream().anyMatch(a -> a.getEstado() == Estado.APROBADO);
			boolean devuelto = aprobadores.stream().anyMatch(a -> a.getEstado() == Estado.DEVUELTO);
			detalle.setPdf(!pdf);
			detalle.setEditar(editar);
			detalle.setDevuelto(devuelto);

			this.getResumen(detalle.getGasto(), cuentas, resumen, Estado.PORCOBRAR);
			this.getResumen(detalle.getRembolso(), cuentas, resumen, Estado.PORPAGAR);

			detalle.setResumen(resumen);

		} else {
			detalle.setIdRdg(0L);
		}

		return detalle;

	}

	@Transactional
	public RdgDto.Publico getRdgPublico(String idRdg) {

		RdgDto.Publico publico = new RdgDto.Publico();
		RdgModel rdgModel = this.rdgDao.findByToken(idRdg).orElseGet(() -> {
			RdgModel nuevo = new RdgModel();
			nuevo.setId(0L);
			return nuevo;
		});
		publico.setId(rdgModel.getId());
		if (publico.getId().longValue() > 0L) {
			publico.setIdSrf(rdgModel.getIdSrf());
			publico.setToken(rdgModel.getToken());
			publico.setNumero(rdgModel.getNumero());
			publico.setGestion(rdgModel.getGestion());
			publico.setNombre(rdgModel.getEmpleado().getPersona().getNombre() + " "
					+ rdgModel.getEmpleado().getPersona().getPaterno() + " "
					+ rdgModel.getEmpleado().getPersona().getMaterno());
			publico.setActividad(rdgModel.getActividad());
			publico.setFecha(rdgModel.getFechaRendicion());
			publico.setAl(rdgModel.getAl().toString().split("T")[0]);
			publico.setDel(rdgModel.getDel().toString().split("T")[0]);
			publico.setDetalle(rdgModel.getDetalle());
			publico.setArea(rdgModel.getArea());
			publico.setCargo(rdgModel.getCargo());
			publico.setCentrocostos(rdgModel.getCentroCostos());
			publico.setEstado(rdgModel.getEstado());
			publico.setCodigo(rdgModel.getCodigo());
			publico.setFormato(rdgModel.getFormato());
			publico.setDevolver(rdgModel.getDevolver());
			publico.setGasto(rdgModel.getGasto());
			publico.setImporte(rdgModel.getImporte());
			publico.setRembolso(rdgModel.getRembolso());
			publico.setSaldo(rdgModel.getSaldo());
			publico.setTc(rdgModel.getTipoCambio());
			publico.setMoneda(rdgModel.getMoneda());
			publico.setParticipantes(rdgModel.getParticipantes());
			publico.setTipoCuenta(rdgModel.getTipoCuenta());
			publico.setBanco(rdgModel.getBanco());
			List<RdgDetalleDto.Response> factura = this.rdgDetalleService
					.findByIdRdgByAgregadoResponse(rdgModel.getId(), Estado.SI);
			publico.setFactura(factura);

			List<AprobacionDto.RdgResponse> aprobadores = this.aprobacionRdgService
					.findByIdRdgResponse(rdgModel.getId());
			publico.setAprobadores(aprobadores);
		}
		return publico;
	}

	private EmpleadoModel getEmpleado(List<EmpleadoModel> empleados, Long idPersona) {
		if (empleados == null || idPersona == null) {
			return crearNuevoEmpleado();
		}

		return empleados.stream()
				.filter(emp -> emp.getPersona() != null && emp.getPersona().getId() != null
						&& emp.getPersona().getId().longValue() == idPersona.longValue())
				.findFirst().orElseGet(() -> crearNuevoEmpleado());
	}

	private EmpleadoModel crearNuevoEmpleado() {
		EmpleadoModel nuevo = new EmpleadoModel();
		nuevo.setId(0L);
		return nuevo;
	}

	private PersonaModel getPersonaPago(List<EmpleadoCargoModel> empleadoCargos, Long idEmpleado) {
		PersonaModel pago = null;
		if (empleadoCargos == null || idEmpleado == null) {
			pago = new PersonaModel();
			pago.setId(0L);
		} else {
			for (EmpleadoCargoModel value : empleadoCargos) {
				if (value.getEmpleado().getId().longValue() == idEmpleado) {
					pago = this.personaService.findByIdpModel(value.getIdpPago());
					break;
				}
			}
		}
		return pago;
	}

	private EmpleadoCargoModel getEmpleadoCargo(List<EmpleadoCargoModel> empleadoCargos, Long idEmpleado) {
		if (empleadoCargos == null || idEmpleado == null) {
			return crearNuevoEmpleadoCargo();
		}

		return empleadoCargos.stream()
				.filter(e -> e.getEmpleado() != null && e.getEmpleado().getId() != null
						&& e.getEmpleado().getId().longValue() == idEmpleado.longValue())
				.findFirst().orElseGet(() -> crearNuevoEmpleadoCargo());
	}

	private EmpleadoCargoModel crearNuevoEmpleadoCargo() {
		EmpleadoCargoModel nuevo = new EmpleadoCargoModel();
		nuevo.setId(0L);
		return nuevo;
	}

	private String getCargo(List<EmpleadoCargoModel> cargos, List<EmpleadoModel> empleados, Long idPersona) {
		String cargo = "";
		for (EmpleadoModel emp : empleados) {
			if (emp.getPersona().getId().longValue() == idPersona.longValue()) {
				for (EmpleadoCargoModel car : cargos) {
					if (car.getEmpleado().getId().longValue() == emp.getId().longValue()) {
						cargo = car.getCargo().getNombre();
						break;
					}
				}
				break;
			}
		}
		return cargo;
	}

	private void getJefes(List<PersonaModel> listaJefes, Long idEmpleadoActual, List<EmpleadoCargoModel> empleadoCargos,
			List<EmpleadoModel> empleados) {

		// buscamos todos los ID para tener la lista de los Jefes y su jerarquia
		List<Long> ids = new ArrayList<Long>();

		while (idEmpleadoActual > 0L) {
			EmpleadoCargoModel jefe = this.getEmpleadoCargo(empleadoCargos, idEmpleadoActual);
			if (jefe.getId().longValue() > 0L && jefe.getJefe().getId().longValue() > 0L) {

				Long idPersonaJefe = jefe.getJefe().getId();

				if (!ids.contains(idPersonaJefe)) {
					ids.add(idPersonaJefe);
					listaJefes.add(jefe.getJefe());

					EmpleadoModel siguiente = this.getEmpleado(empleados, idPersonaJefe);

					if (siguiente.getId().longValue() > 1L) {
						idEmpleadoActual = siguiente.getId();
						continue;
					}
				}
			}
			break;
		}
	}

	private void createAprobacion(List<PersonaModel> listaJefes, RdgModel rdg, List<EmpleadoCargoModel> empleadoCargos,
			List<EmpleadoModel> empleados, PersonaModel idPago, List<Boolean> detalleBol) {

		String token;
		RdgAprobacionModel aprobador = null;
		int i = 0;

		for (PersonaModel jef : listaJefes) {
			i++;
			token = UUID.randomUUID().toString();
			aprobador = new RdgAprobacionModel();
			aprobador.setEstado(i == 1 ? Estado.PENDIENTE : Estado.ESPERA);
			aprobador.setPersona(jef);
			aprobador.setTipo("RDG");
			aprobador.setDestino(i);
			aprobador.setRdg(rdg);
			aprobador.setCargo(this.getCargo(empleadoCargos, empleados, jef.getId()));
			aprobador.setObs(aprobador.getEstado().name());
			aprobador.setToken(token);
			this.aprobacionRdgService.createModel(aprobador);
			if (i == 1) {
				correoRdg(aprobador, rdg, "");
			}
			detalleBol.add(false);
		}
		if (idPago.getId().longValue() > 0L) {
			i++;
			token = UUID.randomUUID().toString();
			aprobador = new RdgAprobacionModel();
			aprobador.setEstado(Estado.ESPERA);
			aprobador.setPersona(idPago);
			aprobador.setTipo("RDG");
			aprobador.setDestino(i);
			aprobador.setRdg(rdg);
			aprobador.setCargo(this.getCargo(empleadoCargos, empleados, idPago.getId()));
			aprobador.setObs("Encargado de Pago");
			aprobador.setToken(token);
			this.aprobacionRdgService.createModel(aprobador);
			detalleBol.add(false);
		}
	}

	private void correoRdg(RdgAprobacionModel aprobador, RdgModel formulario, String isEdit) {

		SettingsModel setting = this.settingsService.getValueModel(1L);

		if (!setting.getNombre().equals("Error")) {
			EmailTemplate template = new EmailTemplate();
			String sujeto = isEdit + (String) formulario.getFormato().get("titulo") + " " + formulario.getNumero() + "-"
					+ formulario.getEmpleado().getPersona().getIdp() + " | " + formulario.getGestion();

			String from = (String) setting.getValue().get("correo");

			Context contexto = template.getEmailRdgPendiente(formulario, setting);

			this.emailService.enviarCorreo("farragot@hotmail.com", sujeto, from, "email-rdg", contexto);
		}
	}

	private void createDetalle(List<RdgDetalleDto.Request> facturas, List<CuentaModel> cuentas, RdgModel rdg,
			Map<String, Object> mapa) {

		for (RdgDetalleDto.Request value : facturas) {
			for (CuentaModel cuenta : cuentas) {
				if (cuenta.getId().longValue() == value.getIdCuenta().longValue()) {
					this.rdgDetalleService.createOrGet(value, cuenta, rdg, mapa);
					break;
				}
			}
		}
	}

	private void createDevolver(List<RdgDevolverDto.Request> deposito, RdgModel rdg) {

		for (RdgDevolverDto.Request value : deposito) {
			this.rdgDevolverService.createOrGet(value, rdg);
		}
	}

	private void updateDetalle(RdgDto.Solicitud response, List<CuentaModel> cuentas, RdgModel rdg) {

		for (RdgDetalleDto.Response value : response.getFactura()) {
			for (CuentaModel cuenta : cuentas) {
				if (cuenta.getId().longValue() == value.getIdCuenta().longValue()) {
					this.rdgDetalleService.updateOrCreate(value, cuenta, rdg, value.getAprobado());
					break;
				}
			}
		}
	}

	private void getResumen(BigDecimal value, List<CuentaModel> cuentas, List<CuentaDto.Resumen> resumen,
			Estado estado) {
		if (value.compareTo(BigDecimal.ZERO) > 0) {
			for (CuentaModel cuenta : cuentas) {
				if (cuenta.getCategoria() == estado) {
					CuentaDto.Resumen r = new CuentaDto.Resumen();
					r.setTipo(cuenta.getTipo());
					r.setCuenta(cuenta.getCuenta());
					r.setNombre(cuenta.getNombre());
					r.setGlosa(cuenta.getGlosa());
					r.setCategoria(cuenta.getCategoria());
					r.setTotal(value);
					resumen.add(r);
					break;
				}
			}
		}
	}

	/** Funciones de Model a Dto */
	private RdgDto.Srf modelToSrf(RdgModel srf) {
		return this.modelMapper.map(srf, RdgDto.Srf.class);
	}

	private RdgDto.Response modelToResponse(RdgModel rdg) {
		return this.modelMapper.map(rdg, RdgDto.Response.class);
	}

	private RdgDto.Lista modelToLista(RdgModel rdg) {
		return this.modelMapper.map(rdg, RdgDto.Lista.class);
	}

}
