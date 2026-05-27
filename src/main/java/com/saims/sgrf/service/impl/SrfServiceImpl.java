package com.saims.sgrf.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.SrfDao;
import com.saims.sgrf.dto.AprobacionDto;
import com.saims.sgrf.dto.PersonaDto;
import com.saims.sgrf.dto.SrfDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.AprobacionModel;
import com.saims.sgrf.model.CuentaBancariaModel;
import com.saims.sgrf.model.EmpleadoCargoModel;
import com.saims.sgrf.model.EmpleadoModel;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.model.SrfModel;
import com.saims.sgrf.model.UsuarioModel;
import com.saims.sgrf.service.AprobacionService;
import com.saims.sgrf.service.CuentaBancariaService;
import com.saims.sgrf.service.EmpleadoCargoService;
import com.saims.sgrf.service.EmpleadoService;
import com.saims.sgrf.service.SrfService;
import com.saims.sgrf.service.UsuarioService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SrfServiceImpl implements SrfService {

	private final SrfDao srfDao;
	private final ModelMapper modelMapper;
	private final UsuarioService usuarioService;
	private final EmpleadoService empleadoService;
	private final EmpleadoCargoService empleadoCargoService;
	private final AprobacionService aprobacionService;
	private final CuentaBancariaService cuentaBancariaService;
	private Personalizer personalizer = new Personalizer();

	@Transactional
	public SrfDto.Response createSrf(SrfDto.Request request) {
		int gestion = java.time.LocalDate.now().getYear();
		int n;

		SrfModel formulario = new SrfModel();

		UsuarioModel usuario = this.usuarioService.getUsuarioModel(request.getIdUsuario());
		CuentaBancariaModel cuenta = this.cuentaBancariaService.getCuentaBancariaActivo(usuario.getPersona().getId());

		List<EmpleadoModel> empleados = this.empleadoService.findByEstado(true);
		List<EmpleadoCargoModel> empleadoCargos = this.empleadoCargoService.findByEstado(true);

		List<PersonaModel> listaJefes = new ArrayList<PersonaModel>();

		EmpleadoModel empleado = this.getEmpleado(empleados, usuario.getPersona().getId());

		List<Long> ids = new ArrayList<Long>();

		Long idEmpleadoActual = empleado.getId();

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

		if (empleado.getId().longValue() > 0) {

			n = this.srfDao.countNumero(empleado.getId(), gestion);

			formulario.setNumero(this.personalizer.getNunero(n));

			formulario.setGestion(gestion);
			formulario.setDetalle(request.getDetalle());
			formulario.setEmpleado(empleado);
			formulario.setFechaSolicitud(request.getFechaSolicitud());
			formulario.setFechaEstimada(this.personalizer.getFecha(request.getFechaEstimada()));
			formulario.setMoneda(request.getMoneda());
			formulario.setMonto(request.getMonto());
			formulario.setTipo("SRF");
			formulario.setCampo_numerico(request.getCampoNumerico());
			formulario.setCampo_texto(request.getCampoTexto());
			formulario.setCodigo(request.getCodigo());
			formulario.setCargo(request.getCargo());
			formulario.setCentrocosto(request.getCentrocosto());
			formulario.setEstado(Estado.PENDIENTE);
			formulario.setCuenta(cuenta);

			this.srfDao.save(formulario);

			int i = 0;
			for (PersonaModel jef : listaJefes) {
				i++;
				AprobacionModel aprobador = new AprobacionModel();
				aprobador.setEstado(i==1 ? Estado.PENDIENTE:Estado.ESPERA);
				aprobador.setPersona(jef);
				aprobador.setTipo("SRF");
				aprobador.setDestino(i);
				aprobador.setSrf(formulario);
				aprobador.setObs(aprobador.getEstado().name());
				this.aprobacionService.createModel(aprobador);
			}
		} else {
			formulario.setId(0L);
		}

		return this.modelToResponse(formulario);
	}
	
	@Transactional
	public List<SrfDto.Response> getSrfUsuarioEstado(Long idUsuario, Estado estado) {
		
		UsuarioModel usuario = this.usuarioService.getUsuarioModel(idUsuario);
		EmpleadoModel empleado = this.empleadoService.findByPersonaIdModel(usuario.getPersona().getId());
		List<SrfDto.Response> lista = this.srfDao.findByIdEmpleadoEstado(empleado.getId(), estado.name())
				.stream()
				.map(srf ->this.modelToResponse(srf))
				.collect(Collectors.toList());
		for(SrfDto.Response value:lista) {
			value.setEmpleado(null);
			value.setAprobadores(null);
			value.setCuenta(null);
		}
		
		return lista;
	}
		
	@Transactional
	public SrfDto.Response getSrf(Long idSrf) {
		SrfModel srfModel = this.srfDao.findById(idSrf).orElseGet(()->{
			SrfModel nuevo = new SrfModel();
			nuevo.setId(0L);
			return nuevo;
		});
		
		SrfDto.Response srf = new SrfDto.Response();
		srf.setId(0L);
		int i=0;
		if(srfModel.getId().longValue()>0L) {
			List<AprobacionModel> lista = this.aprobacionService.findByIdSrf(srfModel.getId());
			List<AprobacionDto.Response> jefes = new ArrayList<AprobacionDto.Response>();
			AprobacionDto.Response nuevo;
			srf.setId(srfModel.getId());
			srf.setCampoNumerico(srfModel.getCampo_numerico());
			srf.setCampoTexto(srfModel.getCampo_texto());
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
			srf.setCuenta(null);
			srf.setEmpleado(null);
			srf.setEdit(true);
			srf.setPdf(true);
			for(AprobacionModel value :lista) {
				nuevo = new AprobacionDto.Response();
				nuevo.setId(value.getId());
				nuevo.setDestino(value.getDestino());
				nuevo.setEstado(value.getEstado());
				nuevo.setTipo(value.getTipo());
				nuevo.setObs(value.getObs());
				nuevo.setFechaActualizacion(value.getFechaActualizacion().toString());
				nuevo.setJefe(this.modelPersonaToResponse(value.getPersona()));
				nuevo.setSrf(null);
				jefes.add(nuevo);
				if(value.getEstado()==Estado.APROBADO) {
					srf.setEdit(false);
					i++;
				}
			}
			srf.setAprobadores(jefes);
			srf.setPdf(i==lista.size()?false:true);
		}
		return srf;
	}
	@Transactional
	public SrfDto.Response updateSrf(SrfDto.Response response) {
		SrfModel update = this.srfDao.findById(response.getId()).orElseGet(()->{
			SrfModel nuevo = new SrfModel();
			nuevo.setId(0L);
			return nuevo;
		});
		if (update.getId().longValue()>0L) {
			update.setFechaEstimada(this.personalizer.getFecha(response.getFechaSolicitud()));
			update.setMonto(response.getMonto());
			update.setMoneda(response.getMoneda());
			update.setDetalle(response.getDetalle());
			update.setCampo_numerico(response.getCampoNumerico());
			update.setCampo_texto(response.getCampoNumerico());
			
			List<AprobacionModel> devuelto = this.aprobacionService.findByIdSrf(update.getId());
			for(AprobacionModel value: devuelto) {
				if(value.getEstado()==Estado.DEVUELTO) {
					value.setEstado(Estado.PENDIENTE);
				}
			}
			this.srfDao.save(update);
		}
		return this.modelToResponse(update);
	}
	@Transactional
	public boolean updateEstado(SrfModel request) {
		SrfModel update = this.srfDao.findById(request.getId()).orElseGet(()->{
			SrfModel nuevo = new SrfModel();
			nuevo.setId(0L);
			return nuevo;
		});
		if (update.getId().longValue()>0L) {
			update.setEstado(request.getEstado());
			this.srfDao.save(update);
			return true;
		}
		else
			return false;
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

	private EmpleadoCargoModel crearNuevoEmpleadoCargo() {
		EmpleadoCargoModel nuevo = new EmpleadoCargoModel();
		nuevo.setId(0L);
		return nuevo;
	}

	private SrfDto.Response modelToResponse(SrfModel formulario) {
		return this.modelMapper.map(formulario, SrfDto.Response.class);
	}
	private PersonaDto.Response modelPersonaToResponse (PersonaModel persona) {
		return this.modelMapper.map(persona, PersonaDto.Response.class);
	}
}
