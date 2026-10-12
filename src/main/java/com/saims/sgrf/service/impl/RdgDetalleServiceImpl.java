package com.saims.sgrf.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.RdgDetalleDao;
import com.saims.sgrf.dto.CuentaDto;
import com.saims.sgrf.dto.RdgDetalleDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.CuentaModel;
import com.saims.sgrf.model.RdgDetalleModel;
import com.saims.sgrf.model.RdgModel;
import com.saims.sgrf.service.RdgDetalleService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RdgDetalleServiceImpl implements RdgDetalleService {

	private final RdgDetalleDao rdgDetalleDao;
	private Personalizer personalizer = new Personalizer();
	private final ModelMapper modelMapper;

	@Transactional
	public RdgDetalleModel createOrGet(RdgDetalleDto.Request request, CuentaModel cuenta, RdgModel rdg,
			Map<String, Object> mapa) {

		String token = UUID.randomUUID().toString();

		RdgDetalleModel detalle = this.rdgDetalleDao.findByIdByToken(request.getIdDetalle(), request.getToken())
				.orElseGet(() -> {
					RdgDetalleModel nuevo = new RdgDetalleModel();
					nuevo.setToken("nuevo");
					return nuevo;
				});

		if (detalle.getToken().equals("nuevo")) {
			// nuevo
			detalle.setArchivo(request.getArchivo());
			detalle.setCuenta(cuenta);
			detalle.setRdg(rdg);
			detalle.setDetalle(request.getDetalle());
			detalle.setDocumento(request.getDocumento());
			detalle.setMoneda(request.getMoneda());
			detalle.setFecha(this.personalizer.getFecha(request.getFecha()));
			detalle.setImporte(request.getImporte());
			detalle.setNumero(request.getNumero());
			detalle.setAgregado(Estado.SI);
			detalle.setAprobado(mapa);
			detalle.setEstado(Estado.PENDIENTE);
			detalle.setToken(token);
			if (request.getAgregado() == Estado.SI)
				this.rdgDetalleDao.save(detalle);
		} else {
			// actualizar
			detalle.setArchivo(request.getArchivo());
			detalle.setCuenta(cuenta);
			detalle.setDetalle(request.getDetalle());
			detalle.setDocumento(request.getDocumento());
			detalle.setFecha(this.personalizer.getFecha(request.getFecha()));
			detalle.setImporte(request.getImporte());
			detalle.setNumero(request.getNumero());
			detalle.setAgregado(request.getAgregado());
			detalle.setAprobado(mapa);
			this.rdgDetalleDao.save(detalle);
		}

		return detalle;
	}
	
	@Transactional
	public RdgDetalleModel updateOrCreate(RdgDetalleDto.Response response, CuentaModel cuenta, RdgModel rdg,
			Map<String, Object> mapa) {

		String token = UUID.randomUUID().toString();

		RdgDetalleModel detalle = this.rdgDetalleDao.findByIdByToken(response.getIdDetalle(), response.getToken())
				.orElseGet(() -> {
					RdgDetalleModel nuevo = new RdgDetalleModel();
					nuevo.setToken("nuevo");
					return nuevo;
				});

		if (detalle.getToken().equals("nuevo")) {
			// nuevo
			detalle.setArchivo(response.getArchivo());
			detalle.setCuenta(cuenta);
			detalle.setRdg(rdg);
			detalle.setDetalle(response.getDetalle());
			detalle.setDocumento(response.getDocumento());
			detalle.setMoneda(response.getMoneda());
			detalle.setFecha(this.personalizer.getFecha(response.getFecha()));
			detalle.setImporte(response.getImporte());
			detalle.setNumero(response.getNumero());
			detalle.setAgregado(response.getAgregado());
			detalle.setAprobado(mapa);
			detalle.setEstado(response.getEstado());
			detalle.setToken(token);
			if (response.getAgregado() == Estado.SI)
				this.rdgDetalleDao.save(detalle);
		} else {
			// actualizar
			detalle.setArchivo(response.getArchivo());
			detalle.setCuenta(cuenta);
			detalle.setDetalle(response.getDetalle());
			detalle.setDocumento(response.getDocumento());
			detalle.setMoneda(response.getMoneda());
			detalle.setFecha(this.personalizer.getFecha(response.getFecha()));
			detalle.setImporte(response.getImporte());
			detalle.setNumero(response.getNumero());
			detalle.setAgregado(response.getAgregado());
			detalle.setAprobado(mapa);
			this.rdgDetalleDao.save(detalle);
		}

		return detalle;
	}

	@Transactional
	public List<RdgDetalleDto.Response> findByIdRdgResponse(Long idRdg) {

		List<RdgDetalleDto.Response> factura = this.rdgDetalleDao.findByIdRdg(idRdg).stream()
				.map(f -> this.modelToResponse(f)).collect(Collectors.toList());

		return factura;
	}

	@Transactional
	public List<RdgDetalleDto.Response> findByIdRdgByAgregadoResponse(Long idRdg, Estado estado) {

		List<RdgDetalleModel> facturas = this.rdgDetalleDao.findByIdRdgByAgregado(idRdg, estado.name());
		List<RdgDetalleDto.Response> factura = new ArrayList<RdgDetalleDto.Response>();
		for (RdgDetalleModel value : facturas) {
			RdgDetalleDto.Response nuevo = new RdgDetalleDto.Response();
			nuevo.setId(value.getId());
			nuevo.setIdDetalle(value.getId());
			nuevo.setFecha(value.getFecha().toString().split("T")[0]);
			nuevo.setDetalle(value.getDetalle());
			nuevo.setImporte(value.getImporte());
			nuevo.setMoneda(value.getMoneda());
			nuevo.setDocumento(value.getDocumento());
			nuevo.setCuenta(this.modelToResponseCuenta(value.getCuenta()));
			nuevo.setIdCuenta(value.getCuenta().getId());
			nuevo.setArchivo(value.getArchivo());
			nuevo.setNumero(value.getNumero());
			nuevo.setAgregado(value.getAgregado());
			nuevo.setAprobado(value.getAprobado());
			nuevo.setEstado(value.getEstado());
			nuevo.setToken(value.getToken());
			factura.add(nuevo);
		}
		
		return factura;
	}

	@Transactional
	public List<RdgDetalleModel> findByIdRdgModel(Long idRdg) {
		List<RdgDetalleModel> factura = this.rdgDetalleDao.findByIdRdg(idRdg);
		return factura;
	}

	@Transactional
	public RdgDetalleModel updateAprobado(Long id, Map<String, Object> aprobado) {

		boolean estado = ((List<Boolean>) aprobado.get("estado")).stream().allMatch(Boolean::booleanValue);

		RdgDetalleModel detalle = this.rdgDetalleDao.findById(id).orElseGet(() -> {
			RdgDetalleModel nuevo = new RdgDetalleModel();
			nuevo.setId(0L);
			return nuevo;
		});

		if (detalle.getId().longValue() > 0L) {
			List<Boolean> anterior = ((List<Boolean>)detalle.getAprobado().get("estado"));
			List<Boolean> actual = ((List<Boolean>)aprobado.get("estado"));
			boolean iguales = anterior.equals(actual);
			if(!estado && iguales) {
				actual.replaceAll(e -> false);
			}
			detalle.setEstado(estado ? Estado.APROBADO : Estado.PENDIENTE);
			detalle.setAprobado(aprobado);
			this.rdgDetalleDao.save(detalle);
		}

		return detalle;
	}
	
	@Transactional
	public RdgDetalleDto.Publico getDocumentoPublico(String idDetalle){
		RdgDetalleDto.Publico documento = new RdgDetalleDto.Publico(); 
		
		RdgDetalleModel detalle = this.rdgDetalleDao.findByTokenAgregado(idDetalle,"SI").orElseGet(() -> {
			RdgDetalleModel nuevo = new RdgDetalleModel();
			nuevo.setId(0L);
			return nuevo;
		});
		documento.setIdDetalle(detalle.getId());
		if(documento.getIdDetalle().longValue()>0L) {
			documento.setFecha(detalle.getFecha().toString().split("T")[0]);
			documento.setImporte(detalle.getImporte());
			documento.setMoneda(detalle.getMoneda());
			documento.setDocumento(detalle.getDocumento());
			documento.setArchivo(detalle.getArchivo());
			documento.setEstado(detalle.getEstado());
			documento.setToken(detalle.getToken());
		}
		return documento;
	}

	private RdgDetalleDto.Response modelToResponse(RdgDetalleModel detalle) {
		return this.modelMapper.map(detalle, RdgDetalleDto.Response.class);
	}
	private CuentaDto.Response modelToResponseCuenta(CuentaModel cuenta) {
		return this.modelMapper.map(cuenta, CuentaDto.Response.class);
	}

}
