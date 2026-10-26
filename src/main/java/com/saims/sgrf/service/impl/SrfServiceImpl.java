package com.saims.sgrf.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.saims.sgrf.config.EmailTemplate;
import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.SrfDao;
import com.saims.sgrf.dto.AprobacionDto;
import com.saims.sgrf.dto.PersonaDto;
import com.saims.sgrf.dto.SrfDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.CuentaBancariaModel;
import com.saims.sgrf.model.EmpleadoCargoModel;
import com.saims.sgrf.model.EmpleadoModel;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.model.SettingsModel;
import com.saims.sgrf.model.SrfAprobacionModel;
import com.saims.sgrf.model.SrfModel;
import com.saims.sgrf.model.UsuarioModel;
import com.saims.sgrf.service.AprobacionService;
import com.saims.sgrf.service.CuentaBancariaService;
import com.saims.sgrf.service.EmailService;
import com.saims.sgrf.service.EmpleadoCargoService;
import com.saims.sgrf.service.EmpleadoService;
import com.saims.sgrf.service.PersonaService;
import com.saims.sgrf.service.SettingsService;
import com.saims.sgrf.service.SrfService;
import com.saims.sgrf.service.UsuarioService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SrfServiceImpl implements SrfService {

	private final SrfDao srfDao;
	private final ModelMapper modelMapper;
	private final EmpleadoService empleadoService;
	private final EmpleadoCargoService empleadoCargoService;
	private final AprobacionService.Srf aprobacionService;
	private final CuentaBancariaService cuentaBancariaService;
	private final PersonaService personaService;
	private final EmailService emailService;
	private final SettingsService settingsService;
	private final UsuarioService usuarioService;
	private Personalizer personalizer = new Personalizer();

	@Transactional
	public SrfDto.Response createSrf(SrfDto.Request request) {

		int gestion = java.time.LocalDate.now().getYear();
		int n;

		SrfModel formulario = new SrfModel();
		String token = UUID.randomUUID().toString();

		CuentaBancariaModel cuenta = this.cuentaBancariaService.getCuentaBancariaActivo(request.getIdPersona());

		// traemos todos los empleados activos
		List<EmpleadoModel> empleados = this.empleadoService.findByEstado(true);

		// traemos todos los cargos activos
		List<EmpleadoCargoModel> empleadoCargos = this.empleadoCargoService.findByEstado(true);

		List<PersonaModel> listaJefes = new ArrayList<PersonaModel>();

		// buscamos los datos del usuario con registro en empleados
		EmpleadoModel empleado = this.getEmpleado(empleados, request.getIdPersona());
		PersonaModel idPago = this.getPersonaPago(empleadoCargos, empleado.getId());

		// buscamos todos los ID para tener la lista de los Jefes y su jerarquia
		List<Long> ids = new ArrayList<Long>();
		Long idEmpleadoActual = empleado.getId();
		int total = 0;

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
		if (idPago.getId().longValue() > 0L) {
			total++;
		}
		total += listaJefes.size();

		if (empleado.getId().longValue() > 0) {

			n = this.srfDao.countNumero(empleado.getId(), gestion);
			request.getFormato().put("total", total);
			formulario.setNumero(this.personalizer.getNunero(n));			
			formulario.setGestion(gestion);
			formulario.setDetalle(request.getDetalle());
			formulario.setEmpleado(empleado);
			formulario.setFechaSolicitud(request.getFechaSolicitud());
			formulario.setFechaEstimada(this.personalizer.getFecha(request.getFechaEstimada()));
			formulario.setMoneda(request.getMoneda());
			formulario.setMonto(request.getMonto());
			formulario.setTipo("SRF");
			formulario.setCampoNumerico(request.getCampoNumerico());
			formulario.setCampoTexto(request.getCampoTexto());
			formulario.setCodigo(request.getCodigo());
			formulario.setCargo(request.getCargo());
			formulario.setCentrocosto(request.getCentrocosto());
			formulario.setEstado(Estado.PENDIENTE);
			formulario.setCuenta(cuenta);
			formulario.setFormato(request.getFormato());
			formulario.setToken(token);

			this.srfDao.save(formulario);
			SrfAprobacionModel aprobador = null;
			int i = 0;
			for (PersonaModel jef : listaJefes) {
				String tokenJefe = UUID.randomUUID().toString();
				i++;
				aprobador = new SrfAprobacionModel();
				aprobador.setEstado(i == 1 ? Estado.PENDIENTE : Estado.ESPERA);
				aprobador.setPersona(jef);
				aprobador.setTipo("SRF");
				aprobador.setDestino(i);
				aprobador.setSrf(formulario);
				aprobador.setCargo(this.getCargo(empleadoCargos, empleados, jef.getId()));
				aprobador.setObs(aprobador.getEstado().name());
				aprobador.setToken(tokenJefe);
				this.aprobacionService.createModel(aprobador);
				if (i == 1) {
					correoSrf(aprobador, formulario,this.personalizer.getSent(empleados, jef.getId()));
				}
			}
			if (idPago.getId().longValue() > 0L) {
				String tokenJefe = UUID.randomUUID().toString();
				i++;
				aprobador = new SrfAprobacionModel();
				aprobador.setEstado(Estado.ESPERA);
				aprobador.setPersona(idPago);
				aprobador.setTipo("SRF");
				aprobador.setDestino(i);
				aprobador.setSrf(formulario);
				aprobador.setCargo(this.getCargo(empleadoCargos, empleados, idPago.getId()));
				aprobador.setObs("Encargado de Pago");
				aprobador.setToken(tokenJefe);
				this.aprobacionService.createModel(aprobador);
			}

		} else {
			formulario.setId(0L);
		}

		return this.modelToResponse(formulario);
	}

	@Transactional
	public List<SrfDto.Stado> getSrfUsuarioEstado(Long idPersona, Estado estado) {

		EmpleadoModel empleado = this.empleadoService.findByPersonaIdModel(idPersona);
		List<SrfModel> lista = this.srfDao.findByIdEmpleadoEstado(empleado.getId(), estado.name());
		List<SrfDto.Stado> srf = new ArrayList<SrfDto.Stado>();
		double nivel = 0;
		double total = 0;
		double ap = 0;
		for (SrfModel value : lista) {
			nivel = (Integer) value.getFormato().get("nivel");
			total = (Integer) value.getFormato().get("total");
			ap = nivel >= 1 ? (nivel / total) * 100 : 10;
			srf.add(this.modelToStado(value));
			srf.getLast().setFechaTitulo((String) value.getFormato().get("fechaTitulo"));
			srf.getLast().setMontoTitulo((String) value.getFormato().get("montoTitulo"));
			srf.getLast().setAprobacion(ap);
		}
		return srf;
	}
	
	@Transactional
	public List<SrfDto.Stado> getSrfUsuarioGestion(Long idUsuario, int gestion) {
		UsuarioModel usuario = this.usuarioService.getUsuarioModel(idUsuario);
		String idp = usuario.getPersona().getIdp();
		EmpleadoModel empleado = this.empleadoService.findByPersonaIdModel(usuario.getPersona().getId());
		List<SrfModel> lista = this.srfDao.getEmpleadoGestion(empleado.getId(), gestion);
		List<SrfDto.Stado> srf = new ArrayList<SrfDto.Stado>();
		double nivel = 0;
		double total = 0;
		double ap = 0;
		for (SrfModel value : lista) {
			nivel = (Integer) value.getFormato().get("nivel");
			total = (Integer) value.getFormato().get("total");
			ap = nivel >= 1 ? (nivel / total) * 100 : 10;
			srf.add(this.modelToStado(value));
			srf.getLast().setIdp(idp);
			srf.getLast().setFechaTitulo((String) value.getFormato().get("fechaTitulo"));
			srf.getLast().setMontoTitulo((String) value.getFormato().get("montoTitulo"));
			srf.getLast().setAprobacion(ap);
		}
		return srf;
	}

	@Transactional
	public SrfModel findById(Long idSrf) {
		return this.srfDao.findById(idSrf).orElseGet(() -> {
			SrfModel nuevo = new SrfModel();
			nuevo.setId(0L);
			return nuevo;
		});
	}

	@Transactional
	public SrfDto.Detalle getSrf(Long idSrf) {

		SrfModel srfModel = this.srfDao.findById(idSrf).orElseGet(() -> {
			SrfModel nuevo = new SrfModel();
			nuevo.setId(0L);
			return nuevo;
		});

		SrfDto.Detalle srf = new SrfDto.Detalle();
		srf.setId(0L);
		int i = 0;
		if (srfModel.getId().longValue() > 0L) {
			List<SrfAprobacionModel> lista = this.aprobacionService.findByIdSrf(srfModel.getId());

			List<AprobacionDto.Detalle> jefes = new ArrayList<AprobacionDto.Detalle>();
			AprobacionDto.Detalle nuevo;
			srf.setId(srfModel.getId());
			srf.setCampoNumerico(srfModel.getCampoNumerico());
			srf.setCampoTexto(srfModel.getCampoTexto());
			srf.setDetalle(srfModel.getDetalle());
			srf.setEstado(srfModel.getEstado());
			srf.setFechaEstimada(srfModel.getFechaEstimada());
			srf.setFechaSolicitud(srfModel.getFechaSolicitud());
			srf.setGestion(srfModel.getGestion());
			srf.setMoneda(srfModel.getMoneda());
			srf.setMonto(srfModel.getMonto());
			srf.setNumero(srfModel.getNumero());
			srf.setTipo(srfModel.getTipo());
			srf.setCargo(srfModel.getCargo());
			srf.setCentrocosto(srfModel.getCentrocosto());
			srf.setCodigo(srfModel.getCodigo());
			srf.setFormato(srfModel.getFormato());
			srf.setToken(srfModel.getToken());
			srf.setCuenta(null);
			srf.setEmpleado(null);
			srf.setEdit(true);
			srf.setPdf(true);
			for (SrfAprobacionModel value : lista) {
				nuevo = new AprobacionDto.Detalle();
				nuevo.setId(value.getId());
				nuevo.setDestino(value.getDestino());
				nuevo.setEstado(value.getEstado());
				nuevo.setTipo(value.getTipo());
				nuevo.setObs(value.getObs());
				nuevo.setFechaActualizacion(value.getFechaActualizacion().toString());
				nuevo.setJefe(this.modelPersonaToResponse(value.getPersona()));
				nuevo.setCargo(value.getCargo());
				nuevo.setSrf(null);
				jefes.add(nuevo);
				if (value.getEstado() == Estado.APROBADO) {
					srf.setEdit(false);
					i++;
				}
			}
			srf.setAprobadores(jefes);
			srf.setPdf(i == lista.size() ? false : true);
		}
		return srf;
	}

	public List<Integer> getSrfEstado(Long idPersona) {
		EmpleadoModel empleado = this.empleadoService.findByPersonaIdModel(idPersona);

		List<Integer> estado = new ArrayList<Integer>();

		if (empleado.getId().longValue() > 0L) {
			List<SrfModel> listaSrf = this.srfDao.findByIdEstado(empleado.getId());
			List<SrfAprobacionModel> listaApr = this.aprobacionService.findByIdPersona(idPersona);
			// Solicitudes
			estado.add((int) listaSrf.stream().filter(s -> s.getEstado() == Estado.PENDIENTE).count());
			estado.add((int) listaSrf.stream().filter(s -> s.getEstado() == Estado.APROBADO).count());
			estado.add((int) listaSrf.stream().filter(s -> s.getEstado() == Estado.RECHAZADO).count());
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
		}
		return estado;

	}

	@Transactional
	public SrfDto.Detalle getSrfUsuario(String idSrf, Long idPersona) {
		return getSrf(idPersona,idSrf);
	}
	
	@Transactional
	public SrfDto.Detalle getSrfAdmin(Long idUsuario,String idSrf) {
		UsuarioModel usuario = this.usuarioService.getUsuarioModel(idUsuario);		
		return getSrf(usuario.getPersona().getId(),idSrf);
	}

	@Transactional
	public SrfDto.Response updateSrf(SrfDto.Response response) {
		SrfModel update = this.srfDao.findById(response.getId()).orElseGet(() -> {
			SrfModel nuevo = new SrfModel();
			nuevo.setId(0L);
			return nuevo;
		});
		if (update.getId().longValue() > 0L) {
			update.setFechaEstimada(this.personalizer.getFecha(response.getFechaSolicitud()));
			update.setMonto(response.getMonto());
			update.setMoneda(response.getMoneda());
			update.setDetalle(response.getDetalle());
			update.setCampoNumerico(response.getCampoNumerico());
			update.setCampoTexto(response.getCampoTexto());
			update.setFormato(response.getFormato());

			List<SrfAprobacionModel> devuelto = this.aprobacionService.findByIdSrf(update.getId());
			if (devuelto.size() > 0) {
				devuelto.get(0).setEstado(Estado.PENDIENTE);
				this.correoSrf(devuelto.get(0), update,"farragot@hotmail.com");
			}
			this.srfDao.save(update);
		}
		return this.modelToResponse(update);
	}

	@Transactional
	public boolean updateEstado(SrfModel request) {
		SrfModel update = this.srfDao.findById(request.getId()).orElseGet(() -> {
			SrfModel nuevo = new SrfModel();
			nuevo.setId(0L);
			return nuevo;
		});
		if (update.getId().longValue() > 0L) {
			update.setEstado(request.getEstado());
			this.srfDao.save(update);
			return true;
		} else
			return false;
	}

	@Transactional
	public SrfDto.Publico getSrfPublico(String token) {

		SrfModel srfModel = this.srfDao.findByToken(token).orElseGet(() -> {
			SrfModel nuevo = new SrfModel();
			nuevo.setId(0L);
			return nuevo;
		});

		SrfDto.Publico srf = new SrfDto.Publico();
		srf.setId(0L);
		if (srfModel.getId().longValue() > 0L) {
			srf.setId(srfModel.getId());
			srf.setCampoNumerico(srfModel.getCampoNumerico());
			srf.setCampoTexto(srfModel.getCampoTexto());
			srf.setDetalle(srfModel.getDetalle());
			srf.setEstado(srfModel.getEstado());
			srf.setFechaEstimada(srfModel.getFechaEstimada());
			srf.setFechaSolicitud(srfModel.getFechaSolicitud());
			srf.setGestion(srfModel.getGestion());
			srf.setMoneda(srfModel.getMoneda());
			srf.setMonto(srfModel.getMonto());
			srf.setNumero(srfModel.getNumero());
			srf.setCodigo(srfModel.getEmpleado().getPersona().getIdp() + " | " + srfModel.getGestion());
			srf.setFormato(srfModel.getFormato());
		}
		return srf;
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

	private EmpleadoCargoModel getEmpleadoCargo(List<EmpleadoCargoModel> empleadoCargos, Long idEmpleado) {
		if (empleadoCargos == null || idEmpleado == null) {
			return crearNuevoEmpleadoCargo();
		}

		return empleadoCargos.stream()
				.filter(e -> e.getEmpleado() != null && e.getEmpleado().getId() != null
						&& e.getEmpleado().getId().longValue() == idEmpleado.longValue())
				.findFirst().orElseGet(() -> crearNuevoEmpleadoCargo());
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

	private EmpleadoCargoModel crearNuevoEmpleadoCargo() {
		EmpleadoCargoModel nuevo = new EmpleadoCargoModel();
		nuevo.setId(0L);
		return nuevo;
	}

	private SrfDto.Response modelToResponse(SrfModel formulario) {
		return this.modelMapper.map(formulario, SrfDto.Response.class);
	}

	private SrfDto.Stado modelToStado(SrfModel formulario) {
		return this.modelMapper.map(formulario, SrfDto.Stado.class);
	}

	private PersonaDto.Response modelPersonaToResponse(PersonaModel persona) {
		return this.modelMapper.map(persona, PersonaDto.Response.class);
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
	
	private void correoSrf(SrfAprobacionModel aprobador, SrfModel formulario, String mail) {

		SettingsModel setting = this.settingsService.getValueModel(1L);

		if (!setting.getNombre().equals("Error")) {
			EmailTemplate template = new EmailTemplate();
			String sujeto = (String) formulario.getFormato().get("titulo") + " " + formulario.getNumero() + "-"
					+ formulario.getEmpleado().getPersona().getIdp() + " | " + formulario.getGestion();
			;
			String from = (String) setting.getValue().get("correo");

			Context contexto = template.getEmailSrfPendiente(formulario, setting);

			this.emailService.enviarCorreo(mail, sujeto, from, "email-srf", contexto);
		}
	}
	
	private SrfDto.Detalle getSrf(Long idPersona,String idSrf) {
		
		EmpleadoModel empleado = this.empleadoService.findByPersonaIdModel(idPersona);

		SrfModel srfModel = this.srfDao.findByToken(idSrf, empleado.getId()).orElseGet(() -> {
			SrfModel nuevo = new SrfModel();
			nuevo.setId(0L);
			return nuevo;
		});

		SrfDto.Detalle srf = new SrfDto.Detalle();
		srf.setId(0L);
		int i = 0;
		if (srfModel.getId().longValue() > 0L) {
			List<SrfAprobacionModel> lista = this.aprobacionService.findByIdSrf(srfModel.getId());

			List<AprobacionDto.Detalle> jefes = new ArrayList<AprobacionDto.Detalle>();
			AprobacionDto.Detalle nuevo;

			srf.setId(srfModel.getId());
			srf.setIdp(empleado.getPersona().getIdp());
			srf.setNombre(empleado.getPersona().getNombre() +" "+empleado.getPersona().getPaterno()+" "+empleado.getPersona().getMaterno());
			srf.setEmail(empleado.getPersona().getEmail());
			srf.setCampoNumerico(srfModel.getCampoNumerico());
			srf.setCampoTexto(srfModel.getCampoTexto());
			srf.setDetalle(srfModel.getDetalle());
			srf.setEstado(srfModel.getEstado());
			srf.setFechaEstimada(srfModel.getFechaEstimada());
			srf.setFechaSolicitud(srfModel.getFechaSolicitud());
			srf.setGestion(srfModel.getGestion());
			srf.setMoneda(srfModel.getMoneda());
			srf.setMonto(srfModel.getMonto());
			srf.setNumero(srfModel.getNumero());
			srf.setTipo(srfModel.getTipo());
			srf.setCargo(srfModel.getCargo());
			srf.setCentrocosto(srfModel.getCentrocosto());
			srf.setCodigo(srfModel.getCodigo());
			srf.setFormato(srfModel.getFormato());
			srf.setToken(srfModel.getToken());
			srf.setCuenta(null);
			srf.setEmpleado(null);
			srf.setEdit(true);
			srf.setPdf(true);
			for (SrfAprobacionModel value : lista) {
				nuevo = new AprobacionDto.Detalle();
				nuevo.setId(value.getId());
				nuevo.setDestino(value.getDestino());
				nuevo.setEstado(value.getEstado());
				nuevo.setTipo(value.getTipo());
				nuevo.setObs(value.getObs());
				nuevo.setFechaActualizacion(value.getFechaActualizacion().toString());
				nuevo.setJefe(this.modelPersonaToResponse(value.getPersona()));
				nuevo.setCargo(value.getCargo());
				nuevo.setSrf(null);
				jefes.add(nuevo);
				if (value.getEstado() == Estado.APROBADO) {
					srf.setEdit(false);
					i++;
				}
			}
			srf.setAprobadores(jefes);
			srf.setPdf(i == lista.size() ? false : true);
		}
		return srf;
	}
}
