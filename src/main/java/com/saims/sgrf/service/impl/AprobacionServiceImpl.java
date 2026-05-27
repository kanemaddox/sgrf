package com.saims.sgrf.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.sgrf.dao.AprobacionDao;
import com.saims.sgrf.dto.AprobacionDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.AprobacionModel;
import com.saims.sgrf.model.SrfModel;
import com.saims.sgrf.model.UsuarioModel;
import com.saims.sgrf.service.AprobacionService;
import com.saims.sgrf.service.SrfService;
import com.saims.sgrf.service.UsuarioService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AprobacionServiceImpl implements AprobacionService {

	private final AprobacionDao aprobacionDao;
	private final UsuarioService usuarioService;
	private final ModelMapper modelMapper;

	@Transactional
	public AprobacionModel createModel(AprobacionModel aprobacion) {
		return this.aprobacionDao.save(aprobacion);
	}

	@Transactional
	public List<AprobacionModel> findByIdSrf(Long idSrf) {
		List<AprobacionModel> lista = this.aprobacionDao.findByIdSrf(idSrf);
		return lista;
	}

	@Transactional
	public List<AprobacionModel> getPersonaEstado(Long idPersona, Estado estado) {
		List<AprobacionModel> lista = this.aprobacionDao.getPersonaEstado(idPersona, estado.name());
		return lista;
	}

	@Transactional
	public List<AprobacionDto.Pendiente> getPendientes(Long idUsuario, Estado estado) {
		
		UsuarioModel usuario = this.usuarioService.getUsuarioModel(idUsuario);
		List<AprobacionDto.Pendiente> pendientes = this.aprobacionDao
				.getPersonaEstado(usuario.getPersona().getId(), estado.name()).stream()
				.map(pendiente -> this.modelToPendiente(pendiente)).collect(Collectors.toList());
		return pendientes;
	}

	@Transactional
	public AprobacionDto.Revisar getApr(Long idUsuario, Long idApr) {
		UsuarioModel usuario = this.usuarioService.getUsuarioModel(idUsuario);
		AprobacionDto.Revisar revisar = this.aprobacionDao.getIdAprIdPersona(idApr, usuario.getPersona().getId())
				.map(re -> this.modelToRevizar(re)).orElseGet(() -> {
					AprobacionModel nuevo = new AprobacionModel();
					nuevo.setId(0L);
					return this.modelToRevizar(nuevo);
				});

		if (revisar.getId().longValue() > 0L) {
			List<AprobacionDto.Autorizador> autorizador = this.aprobacionDao.findByIdSrf(revisar.getSrf().getId())
					.stream().map(aut -> this.modelToAutorizador(aut)).collect(Collectors.toList());
			revisar.getSrf().setAutorizador(autorizador);
		}
		return revisar;
	}

	@Transactional
	public AprobacionDto.Response updateAprobacion(AprobacionDto.UpdateRequest request) {
		UsuarioModel usuario = this.usuarioService.getUsuarioModel(request.getIdPersona());
		List<AprobacionModel> lista = this.aprobacionDao.findByIdSrf(request.getIdSrf());
		AprobacionDto.Response response = new AprobacionDto.Response();
		response.setId(0L);
		int estado = 0;
		boolean rechazar = false;
		for (AprobacionModel value : lista) {
			if (value.getId().longValue() == request.getId().longValue()
					&& value.getPersona().getId().longValue() == usuario.getPersona().getId().longValue()) {
				value.setEstado(request.getEstado());
				value.setObs(request.getObs());
				this.aprobacionDao.save(value);
			}
			if (request.getEstado() == Estado.APROBADO && value.getDestino() == request.getDestino() + 1
					&& value.getEstado() == Estado.ESPERA) {
				value.setEstado(Estado.PENDIENTE);
				value.setObs(Estado.APROBADO + " : " + usuario.getPersona().getNombre());
				this.aprobacionDao.save(value);
				response = this.modelToResponse(value);
			}
			if(value.getEstado() == Estado.APROBADO) {
				estado++;
			}
			if(value.getEstado() == Estado.RECHAZADO) {
				rechazar=true;
			}
		}
		if(estado == lista.size()) {
			AprobacionModel apr = lista.get(0);
			apr.getSrf().setEstado(Estado.APROBADO);
			this.aprobacionDao.save(apr);
		}
		if(rechazar) {
			AprobacionModel apr = lista.get(0);
			apr.getSrf().setEstado(Estado.RECHAZADO);
			this.aprobacionDao.save(apr);
		}
		
		response.setSrf(null);
		return response;
	}

	private AprobacionDto.Pendiente modelToPendiente(AprobacionModel value) {
		return this.modelMapper.map(value, AprobacionDto.Pendiente.class);
	}

	private AprobacionDto.Revisar modelToRevizar(AprobacionModel value) {
		return this.modelMapper.map(value, AprobacionDto.Revisar.class);
	}

	private AprobacionDto.Autorizador modelToAutorizador(AprobacionModel value) {
		return this.modelMapper.map(value, AprobacionDto.Autorizador.class);
	}
	private AprobacionDto.Response modelToResponse(AprobacionModel value){
		return this.modelMapper.map(value, AprobacionDto.Response.class);
	}

}
