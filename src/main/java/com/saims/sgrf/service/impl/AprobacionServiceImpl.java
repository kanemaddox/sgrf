package com.saims.sgrf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.saims.sgrf.config.EmailTemplate;
import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.AprobacionDao;
import com.saims.sgrf.dto.AprobacionDto;
import com.saims.sgrf.dto.PersonaDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.EmpleadoModel;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.model.SettingsModel;
import com.saims.sgrf.model.SrfAprobacionModel;
import com.saims.sgrf.model.SrfModel;
import com.saims.sgrf.service.AprobacionService;
import com.saims.sgrf.service.EmailService;
import com.saims.sgrf.service.EmpleadoService;
import com.saims.sgrf.service.PersonaService;
import com.saims.sgrf.service.RdgService;
import com.saims.sgrf.service.SettingsService;
import com.saims.sgrf.service.UsuarioService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AprobacionServiceImpl implements AprobacionService.Srf {

	private final AprobacionDao aprobacionDao;
	private final UsuarioService usuarioService;
	private final ModelMapper modelMapper;
	private final RdgService rdgService;
	private final EmailService emailService;
	private final SettingsService settingsService;
	private final PersonaService personaService;
	//Temporar solo paar pruebas por correo 
	private final EmpleadoService empleadoService;
	private Personalizer personalizer = new Personalizer();

	@Transactional
	public SrfAprobacionModel createModel(SrfAprobacionModel aprobacion) {
		return this.aprobacionDao.save(aprobacion);
	}

	@Transactional
	public List<SrfAprobacionModel> findByIdSrf(Long idSrf) {
		List<SrfAprobacionModel> lista = this.aprobacionDao.findByIdSrf(idSrf);
		return lista;
	}
	
	
	@Transactional
	public List<SrfAprobacionModel> findByIdPersona(Long idPersona) {
		List<SrfAprobacionModel> lista = this.aprobacionDao.findByIdPersona(idPersona);
		return lista;
	}

	@Transactional
	public List<SrfAprobacionModel> getPersonaEstado(Long idPersona, Estado estado) {
		List<SrfAprobacionModel> lista = this.aprobacionDao.getPersonaEstado(idPersona, estado.name());
		return lista;
	}

	@Transactional
	public List<AprobacionDto.Stado> getSrfEstado(Long idPersona, Estado estado) {

		List<SrfAprobacionModel> aprobaciones = this.aprobacionDao.getPersonaEstado(idPersona, estado.name());
		List<AprobacionDto.Stado> lista = new ArrayList<AprobacionDto.Stado>();
		for(SrfAprobacionModel value : aprobaciones) {
			lista.add(this.modelToStado(value));
			
			lista.getLast().setNombre(value.getSrf().getEmpleado().getPersona().getNombre());
			lista.getLast().setIdp(value.getSrf().getEmpleado().getPersona().getIdp());
			lista.getLast().setFoto(value.getSrf().getEmpleado().getPersona().getFoto());
			
			lista.getLast().setNumero(value.getSrf().getNumero());
			lista.getLast().setGestion(value.getSrf().getGestion());
			lista.getLast().setFechaSolicitud(value.getSrf().getFechaSolicitud());
			lista.getLast().setFechaEstimada(value.getSrf().getFechaEstimada());
			lista.getLast().setMonto(value.getSrf().getMonto());
			lista.getLast().setMoneda(value.getSrf().getMoneda());
			lista.getLast().setDetalle(value.getSrf().getDetalle());
			lista.getLast().setCampoTexto(value.getSrf().getCampoTexto());
			lista.getLast().setCampoNumerico(value.getSrf().getCampoNumerico());
			lista.getLast().setTokenSrf(value.getSrf().getToken());
			lista.getLast().setFechaTitulo((String)value.getSrf().getFormato().get("fechaTitulo"));
			lista.getLast().setMontoTitulo((String)value.getSrf().getFormato().get("montoTitulo"));
			
		}
		return lista;
	}

	@Transactional
	public AprobacionDto.Revisar getApr(Long idPersona, String idApr) {

		SrfAprobacionModel aprobacion = this.aprobacionDao.getTokenIdPersona(idApr, idPersona).orElseGet(()->{
			SrfAprobacionModel nuevo = new SrfAprobacionModel();
			nuevo.setId(0L);
			return nuevo;
		});
		
		AprobacionDto.Revisar revisar = new AprobacionDto.Revisar();
		revisar.setId(0L);
		
		if(aprobacion.getId().longValue()>0L) {
			//Aprobacion
			revisar.setId(aprobacion.getId());
			revisar.setToken(aprobacion.getToken());
			revisar.setDestino(aprobacion.getDestino());
			revisar.setEstado(aprobacion.getEstado());
			revisar.setTipo(aprobacion.getTipo());
			revisar.setObs(aprobacion.getObs());
			revisar.setFechaCreacion(aprobacion.getFechaCreacion().toString());
			revisar.setFechaActualizacion(aprobacion.getFechaActualizacion().toString());
			
			
			// Fromato del formulario
			revisar.setTitulo((String)aprobacion.getSrf().getFormato().get("titulo"));
			revisar.setSeccionUno((String)aprobacion.getSrf().getFormato().get("seccionUno"));
			revisar.setSeccionDos((String)aprobacion.getSrf().getFormato().get("seccionDos"));
			revisar.setFechaTitulo((String)aprobacion.getSrf().getFormato().get("fechaTitulo"));
			revisar.setMontoTitulo((String)aprobacion.getSrf().getFormato().get("montoTitulo"));
			revisar.setDescripcion((String)aprobacion.getSrf().getFormato().get("descripcion"));
			revisar.setEstadoComplemento((Boolean)aprobacion.getSrf().getFormato().get("estadoComplemento"));
			revisar.setTrueComplemento((Boolean)aprobacion.getSrf().getFormato().get("trueComplemento"));
			revisar.setComplemento((String)aprobacion.getSrf().getFormato().get("complemento"));
			revisar.setDetalleComplemento((String)aprobacion.getSrf().getFormato().get("detalleComplemento"));
			revisar.setCantidadComplemento((String)aprobacion.getSrf().getFormato().get("cantidadComplemento"));
			
			revisar.setSeccionTres((String)aprobacion.getSrf().getFormato().get("seccionTres"));
			revisar.setEstadoTres((Boolean)aprobacion.getSrf().getFormato().get("estadoTres"));
			revisar.setDetalleFormato((String)aprobacion.getSrf().getFormato().get("detalle"));
			
			// empleado
			revisar.setNombre(aprobacion.getSrf().getEmpleado().getPersona().getNombre()+ " "+aprobacion.getSrf().getEmpleado().getPersona().getPaterno()+" "+aprobacion.getSrf().getEmpleado().getPersona().getMaterno());
			revisar.setIdp(aprobacion.getSrf().getEmpleado().getPersona().getIdp());
			revisar.setCargo(aprobacion.getSrf().getCargo());
			revisar.setCodigo(aprobacion.getSrf().getCodigo());
			revisar.setCentro(aprobacion.getSrf().getCentrocosto());
			
			//SRF
			revisar.setIdSrf(aprobacion.getSrf().getId());
			revisar.setNumero(aprobacion.getSrf().getNumero());
			revisar.setGestion(aprobacion.getSrf().getGestion());
			revisar.setFechaSolicitud(aprobacion.getSrf().getFechaSolicitud());
			revisar.setFechaEstimada(aprobacion.getSrf().getFechaEstimada());
			revisar.setMonto(aprobacion.getSrf().getMonto());
			revisar.setMoneda(aprobacion.getSrf().getMoneda());
			revisar.setDetalle(aprobacion.getSrf().getDetalle());
			revisar.setCampoTexto(aprobacion.getSrf().getCampoTexto());
			revisar.setCampoNumerico(aprobacion.getSrf().getCampoNumerico());
			revisar.setTokenSrf(aprobacion.getSrf().getToken());
			
			List<AprobacionDto.Detalle>autorizador = new ArrayList<AprobacionDto.Detalle>();
			//Autorizadores
			List<SrfAprobacionModel> aprobadores = this.aprobacionDao.findByIdSrf(revisar.getIdSrf());
			
			for(SrfAprobacionModel value : aprobadores) {
				AprobacionDto.Detalle nuevo = new AprobacionDto.Detalle();
				nuevo.setId(value.getId());
				nuevo.setDestino(value.getDestino());
				nuevo.setEstado(value.getEstado());
				nuevo.setJefe(modelToResponsePersona(value.getPersona()));
				nuevo.setObs(value.getObs());
				autorizador.add(nuevo);
			}
			revisar.setAutorizador(autorizador);	
		}
		return revisar;
	}

	@Transactional
	public AprobacionDto.Response updateAprobacion(AprobacionDto.UpdateRequest request) {
		// buscamos al Usuario
		PersonaDto.Response persona = this.personaService.getById(request.getIdPersona());
		List<EmpleadoModel>empleado = this.empleadoService.findAll();

		// Traemos la lista de Autorizadores de la Solicitud
		List<SrfAprobacionModel> lista = this.aprobacionDao.findByIdSrf(request.getIdSrf());

		AprobacionDto.Response response = new AprobacionDto.Response();
		response.setId(0L);
		int estado = 0;

		if (request.getEstado() == Estado.RECHAZADO) {
			for (SrfAprobacionModel value : lista) {
				if (value.getDestino() <= request.getDestino()) {
					value.setEstado(request.getEstado());
					value.setObs(request.getObs() + " | " + persona.getNombre());
					this.aprobacionDao.save(value);
				}
			}
			SrfAprobacionModel apr = lista.get(0);
			apr.getSrf().setEstado(Estado.RECHAZADO);
			apr.getSrf().getFormato().put("nivel", 0);
			this.aprobacionDao.save(apr);
			this.correoSrf(apr.getSrf(),Estado.RECHAZADO,this.personalizer.getSent(empleado, apr.getSrf().getEmpleado().getPersona().getId()));
		} else {
			if (request.getEstado() == Estado.DEVUELTO) {
				for (SrfAprobacionModel value : lista) {
					if (value.getDestino() <= request.getDestino()) {
						value.setEstado(request.getEstado());
						value.setObs(request.getObs() + " | " + persona.getNombre());
						value.getSrf().getFormato().put("nivel",0);
						this.aprobacionDao.save(value);
					}
				}
				this.correoSrf(lista.get(0).getSrf(),Estado.DEVUELTO,this.personalizer.getSent(empleado,lista.get(0).getSrf().getEmpleado().getPersona().getId()));
			} else {
				for (SrfAprobacionModel value : lista) {
					if (value.getId().longValue() == request.getId().longValue()
							&& value.getPersona().getId().longValue() == persona.getId().longValue()) {
						value.setEstado(request.getEstado());
						value.setObs(request.getObs());
						value.getSrf().getFormato().put("nivel",value.getDestino());
						this.aprobacionDao.save(value);
					}

					if (request.getEstado() == Estado.APROBADO && value.getDestino() == (request.getDestino() + 1)
							&& (value.getEstado() == Estado.ESPERA || value.getEstado() == Estado.DEVUELTO)) {

						value.setEstado(Estado.PENDIENTE);
						value.setObs(Estado.APROBADO + " : " + persona.getNombre());
						this.aprobacionDao.save(value);
						
						this.correoSrf(value.getSrf(),Estado.PENDIENTE,this.personalizer.getSent(empleado, value.getPersona().getId())); 						
						response = this.modelToResponse(value);
					}
					if (value.getEstado() == Estado.APROBADO) {
						estado++;
					}
				}
			}
		}

		if (estado == lista.size()) {
			SrfAprobacionModel apr = lista.get(0);
			apr.getSrf().setEstado(Estado.APROBADO);
			this.aprobacionDao.save(apr);
			this.rdgService.createTramite(apr.getSrf(), apr.getSrf().getEmpleado());
			
			this.correoSrf(apr.getSrf(),Estado.APROBADO,this.personalizer.getSent(empleado, apr.getSrf().getEmpleado().getPersona().getId()));
			
		}
		response.setSrf(null);
		return response;
	}

	private AprobacionDto.Stado modelToStado(SrfAprobacionModel value) {
		return this.modelMapper.map(value, AprobacionDto.Stado.class);
	}
	
	private PersonaDto.Response modelToResponsePersona(PersonaModel value) {
		return this.modelMapper.map(value, PersonaDto.Response.class);
	}

	private AprobacionDto.Revisar modelToRevizar(SrfAprobacionModel value) {
		return this.modelMapper.map(value, AprobacionDto.Revisar.class);
	}

	private AprobacionDto.Autorizador modelToAutorizador(SrfAprobacionModel value) {
		return this.modelMapper.map(value, AprobacionDto.Autorizador.class);
	}

	private AprobacionDto.Response modelToResponse(SrfAprobacionModel value) {
		return this.modelMapper.map(value, AprobacionDto.Response.class);
	}

	private void correoSrf(SrfModel formulario, Estado estado,String correo) {

		SettingsModel setting = this.settingsService.getValueModel(1L);

		if (!setting.getNombre().equals("Error")) {
			EmailTemplate template = new EmailTemplate();
			String from = (String) setting.getValue().get("correo");
			String sujeto = "";
			String emailHtml="";
			Context contexto = null;
			
			switch(estado) {
				case APROBADO:
					sujeto ="Solicitud No. " + formulario.getNumero() + " APROBADO";
					contexto = template.getEmailSrfEstado(formulario, setting, estado);
					emailHtml="email-srf-estado";
					break;
				case PENDIENTE:
					sujeto = (String) formulario.getFormato().get("titulo") + " " + formulario.getNumero() + "-"
							+ formulario.getEmpleado().getPersona().getIdp() + " | " + formulario.getGestion();
					contexto = template.getEmailSrfPendiente(formulario, setting);
					emailHtml="email-srf";
					break;
				case DEVUELTO:
					sujeto ="Solicitud No. " + formulario.getNumero() + " DEVUELTO";
					contexto = template.getEmailSrfEstado(formulario, setting, estado);
					emailHtml="email-srf-estado";
					break;
				case RECHAZADO:
					sujeto ="Solicitud No. " + formulario.getNumero() + " RECHAZADO";
					contexto = template.getEmailSrfEstado(formulario, setting, estado);
					emailHtml="email-srf-estado";
					break;
			}
			
			this.emailService.enviarCorreo(correo, sujeto, from,emailHtml, contexto);
			
		}
	}
	
}
