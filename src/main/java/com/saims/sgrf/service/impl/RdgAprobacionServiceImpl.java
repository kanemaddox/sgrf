package com.saims.sgrf.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.saims.sgrf.config.EmailTemplate;
import com.saims.sgrf.dao.RdgAprobacionDao;
import com.saims.sgrf.dto.AprobacionDto;
import com.saims.sgrf.dto.CuentaDto;
import com.saims.sgrf.dto.RdgDetalleDto;
import com.saims.sgrf.dto.RdgDevolverDto;
import com.saims.sgrf.dto.RdgDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.CuentaModel;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.model.RdgAprobacionModel;
import com.saims.sgrf.model.RdgModel;
import com.saims.sgrf.model.SettingsModel;
import com.saims.sgrf.service.AprobacionService;
import com.saims.sgrf.service.CuentaService;
import com.saims.sgrf.service.EmailService;
import com.saims.sgrf.service.RdgDetalleService;
import com.saims.sgrf.service.RdgDevolverService;
import com.saims.sgrf.service.SettingsService;
import com.saims.sgrf.service.UsuarioService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class RdgAprobacionServiceImpl implements AprobacionService.Rdg {

	private final RdgAprobacionDao rdgAprobacionDao;
	private final ModelMapper modelMapper;
	private final UsuarioService usuarioService;
	private final RdgDetalleService rdgDetalleService;
	private final CuentaService cuentaService;
	private final SettingsService settingsService;
	private final EmailService emailService;
	private final RdgDevolverService rdgDevolverService;

	@Transactional
	public RdgAprobacionModel createModel(RdgAprobacionModel aprobacion) {
		return this.rdgAprobacionDao.save(aprobacion);
	}

	@Transactional
	public List<AprobacionDto.RdgResponse> findByIdRdgResponse(Long idRdg) {
		List<AprobacionDto.RdgResponse> lista = this.rdgAprobacionDao.findByIdRdg(idRdg).stream()
				.map(l -> this.modelToResponse(l)).collect(Collectors.toList());
		return lista;
	}
	
	@Transactional
	public List<RdgAprobacionModel> findByIdRdgModel(Long idRdg) {
		List<RdgAprobacionModel> lista = this.rdgAprobacionDao.findByIdRdg(idRdg);
		return lista;
	}

	@Transactional
	public List<RdgAprobacionModel> findByIdPersona(Long idPersona) {
		List<RdgAprobacionModel> lista = this.rdgAprobacionDao.findByIdPersona(idPersona);
		return lista;
	}

	@Transactional
	public List<RdgDto.ListaRevisar> getEstadoRdg(Long idPersona, Estado estado) {

		List<RdgAprobacionModel> aprobacion = this.rdgAprobacionDao.findByIdPersonaByEstado(idPersona, estado.name());

		List<RdgDto.ListaRevisar> lista = new ArrayList<RdgDto.ListaRevisar>();
		RdgDto.ListaRevisar revisar = null;
		for (RdgAprobacionModel value : aprobacion) {

			revisar = new RdgDto.ListaRevisar();
			revisar.setIdAprobacion(value.getId());
			revisar.setId(value.getRdg().getId());
			revisar.setDetalle(value.getRdg().getDetalle());
			revisar.setNumero(value.getRdg().getNumero());
			revisar.setGestion(value.getRdg().getGestion());
			revisar.setImporte(value.getRdg().getImporte());
			revisar.setGasto(value.getRdg().getGasto());
			revisar.setRembolso(value.getRdg().getRembolso());
			revisar.setSaldo(value.getRdg().getSaldo());
			revisar.setDevolver(value.getRdg().getDevolver());
			revisar.setMoneda(value.getRdg().getMoneda());
			revisar.setEstado(value.getEstado());
			revisar.setFecha(value.getRdg().getFechaRendicion());

			revisar.setNombre(value.getRdg().getEmpleado().getPersona().getNombre());
			revisar.setIdp(value.getRdg().getEmpleado().getPersona().getIdp());
			revisar.setFoto(value.getRdg().getEmpleado().getPersona().getFoto());
			revisar.setToken(value.getToken());

			lista.add(revisar);
		}
		return lista;
	}

	@Transactional
	public RdgDto.Revisar getRdgRevisar(Long idPersona, String idRdg) {

		RdgAprobacionModel aprobacion = this.rdgAprobacionDao.findByIdPersonaByToken(idRdg, idPersona).orElseGet(() -> {
			RdgAprobacionModel nuevo = new RdgAprobacionModel();
			nuevo.setId(0L);
			return nuevo;
		});

		RdgDto.Revisar revisar = new RdgDto.Revisar();
		if (aprobacion.getId().longValue() > 0L) {
			List<CuentaModel> cuentas = this.cuentaService.findAll();
			List<CuentaDto.Resumen> resumen = new ArrayList<CuentaDto.Resumen>();

			String nombre = aprobacion.getRdg().getEmpleado().getPersona().getNombre() + " "
					+ aprobacion.getRdg().getEmpleado().getPersona().getPaterno() + " "
					+ aprobacion.getRdg().getEmpleado().getPersona().getMaterno();

			revisar.setIdRdg(aprobacion.getRdg().getId());
			revisar.setIdSrf(aprobacion.getRdg().getIdSrf());

			revisar.setIdAprobacion(aprobacion.getId());
			revisar.setEstadoAprobacion(aprobacion.getEstado());
			revisar.setDestinoAprobacion(aprobacion.getDestino());

			revisar.setNumero(aprobacion.getRdg().getNumero());
			revisar.setGestion(aprobacion.getRdg().getGestion());
			revisar.setActividad(aprobacion.getRdg().getActividad());
			revisar.setFecha(aprobacion.getRdg().getFechaRendicion());
			revisar.setAl(aprobacion.getRdg().getAl().toString());
			revisar.setDel(aprobacion.getRdg().getDel().toString());
			revisar.setDetalle(aprobacion.getRdg().getDetalle());
			revisar.setArea(aprobacion.getRdg().getArea());
			revisar.setCargo(aprobacion.getRdg().getCargo());
			revisar.setCentrocostos(aprobacion.getRdg().getCentroCostos());
			revisar.setCodigo(aprobacion.getRdg().getCodigo());
			revisar.setNombre(nombre);
			revisar.setIdp(aprobacion.getRdg().getEmpleado().getPersona().getIdp());
			revisar.setEstado(aprobacion.getRdg().getEstado());
			revisar.setFormato(aprobacion.getRdg().getFormato());

			revisar.setDevolver(aprobacion.getRdg().getDevolver());
			revisar.setGasto(aprobacion.getRdg().getGasto());
			revisar.setImporte(aprobacion.getRdg().getImporte());
			revisar.setRembolso(aprobacion.getRdg().getRembolso());
			revisar.setSaldo(aprobacion.getRdg().getSaldo());
			revisar.setTc(aprobacion.getRdg().getTipoCambio());
			revisar.setMoneda(aprobacion.getRdg().getMoneda());

			revisar.setParticipantes(aprobacion.getRdg().getParticipantes());
			revisar.setTipoCuenta(aprobacion.getRdg().getTipoCuenta());
			revisar.setCuenta(aprobacion.getRdg().getCuenta());
			revisar.setBanco(aprobacion.getRdg().getBanco());

			List<RdgDetalleDto.Response> factura = this.rdgDetalleService
					.findByIdRdgByAgregadoResponse(revisar.getIdRdg(), Estado.SI);

			revisar.setFactura(factura);

			List<AprobacionDto.RdgResponse> aprobadores = this.findByIdRdgResponse(revisar.getIdRdg());

			revisar.setAprobadores(aprobadores);
			
			if(revisar.getDevolver().compareTo(BigDecimal.ZERO)>0) {
				List<RdgDevolverDto.Response>deposito = this.rdgDevolverService.findByIdRdgByAgregadoResponse(revisar.getIdRdg(), Estado.SI);
				revisar.setDeposito(deposito);
			}

			this.getResumen(revisar.getGasto(), cuentas, resumen, Estado.PORPAGAR);
			this.getResumen(revisar.getRembolso(), cuentas, resumen, Estado.PORCOBRAR);

			revisar.setResumen(resumen);

		} else {
			revisar.setIdRdg(0L);
		}
		return revisar;
	}

	@Transactional
	public RdgDto.Revisar updateAprobacion(AprobacionDto.AprobacionRdg response) {

		List<RdgAprobacionModel> aprobaciones = this.rdgAprobacionDao.findByIdRdg(response.getIdRdg());
		PersonaModel persona = null;

		int estado = 0;

		if (response.getEstado() == Estado.RECHAZADO) {
			for (RdgAprobacionModel value : aprobaciones) {
				if (value.getDestino() <= response.getDestino()) {
					value.setEstado(response.getEstado());
					value.setObs(response.getObs() + " | " + value.getPersona().getNombre());
					this.rdgAprobacionDao.save(value);
				}
			}
			RdgAprobacionModel apr = aprobaciones.get(0);
			apr.getRdg().setEstado(Estado.RECHAZADO);
			this.rdgAprobacionDao.save(apr);
			this.correoRdg(aprobaciones.get(0), aprobaciones.get(0).getRdg(), Estado.RECHAZADO);

		} else {
			if (response.getEstado() == Estado.DEVUELTO) {
				for (RdgAprobacionModel value : aprobaciones) {
					if (value.getDestino() <= response.getDestino()) {
						value.setEstado(response.getEstado());
						value.setObs(response.getObs() + " | " + value.getPersona().getNombre());
						this.rdgAprobacionDao.save(value);
					}
				}
				this.correoRdg(aprobaciones.get(0), aprobaciones.get(0).getRdg(), Estado.DEVUELTO);
			} else {
				for (RdgAprobacionModel value : aprobaciones) {
					if (value.getId().longValue() == response.getIdAprobacion().longValue()
							&& value.getPersona().getId().longValue() == response.getIdPersona().longValue()) {

						value.setEstado(response.getEstado());
						value.setObs(response.getObs());
						persona = value.getPersona();
						this.rdgAprobacionDao.save(value);
					}

					if (response.getEstado() == Estado.APROBADO && value.getDestino() == (response.getDestino() + 1)
							&& (value.getEstado() == Estado.ESPERA || value.getEstado() == Estado.DEVUELTO)) {

						value.setEstado(Estado.PENDIENTE);
						value.setObs(Estado.APROBADO + " BY : " + persona.getNombre());
						this.rdgAprobacionDao.save(value);

						this.correoRdg(value, value.getRdg(), Estado.PENDIENTE);
					}
					if (value.getEstado() == Estado.APROBADO) {
						estado++;
					}
				}
			}
		}

		for (RdgDetalleDto.Response value : response.getFactura()) {
			// calculamos true, volvemos false todos 
			this.rdgDetalleService.updateAprobado(value.getId(), value.getAprobado());
		}

		if (estado == aprobaciones.size()) {
			RdgAprobacionModel apr = aprobaciones.get(0);
			apr.getRdg().setEstado(Estado.APROBADO);
			this.rdgAprobacionDao.save(apr);

			this.correoRdg(apr, apr.getRdg(), Estado.APROBADO);
		}
		return null;
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

	private void correoRdg(RdgAprobacionModel aprobador, RdgModel formulario, Estado estado) {

		SettingsModel setting = this.settingsService.getValueModel(1L);

		if (!setting.getNombre().equals("Error")) {
			EmailTemplate template = new EmailTemplate();
			String from = (String) setting.getValue().get("correo");
			String sujeto = "";
			String emailHtml = "";
			String correo = "farragot@hotmail.com";
			Context contexto = null;

			switch (estado) {
				case APROBADO:
					sujeto = "Estado de Formulario No. " + formulario.getNumero() + " APROBADO";
					contexto = template.getEmailRdgEstado(formulario, setting, estado);
					emailHtml = "email-rdg-estado";
					break;
				case PENDIENTE:
					sujeto = (String) formulario.getFormato().get("titulo") + " " + formulario.getNumero() + "-"
							+ formulario.getEmpleado().getPersona().getIdp() + " | " + formulario.getGestion();
	
					contexto = template.getEmailRdgPendiente(formulario, setting);
					emailHtml = "email-rdg";
					break;
				case DEVUELTO:
					sujeto = "Estado de Formulario No. " + formulario.getNumero() + " DEVUELTO";
					contexto = template.getEmailRdgEstado(formulario, setting, estado);
					emailHtml = "email-rdg-estado";
					break;
				case RECHAZADO:
					sujeto = "Estado de Formulario No. " + formulario.getNumero() + " RECHAZADO";
					contexto = template.getEmailRdgEstado(formulario, setting, estado);
					emailHtml = "email-rdg-estado";
					break;
			}

			this.emailService.enviarCorreo(correo, sujeto, from, emailHtml, contexto);
		}
	}

	private AprobacionDto.RdgResponse modelToResponse(RdgAprobacionModel rdgAprobacion) {
		return this.modelMapper.map(rdgAprobacion, AprobacionDto.RdgResponse.class);
	}

	private RdgDto.ListaRevisar modelToLista(RdgModel rdg) {
		return this.modelMapper.map(rdg, RdgDto.ListaRevisar.class);
	}
}
