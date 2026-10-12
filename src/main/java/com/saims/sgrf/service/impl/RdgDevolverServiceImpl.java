package com.saims.sgrf.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.RdgDevolverDao;
import com.saims.sgrf.dto.RdgDevolverDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.RdgDevolverModel;
import com.saims.sgrf.model.RdgModel;
import com.saims.sgrf.service.RdgDevolverService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RdgDevolverServiceImpl implements RdgDevolverService {

	private final RdgDevolverDao rdgDevolverDao;
	private Personalizer personalizer = new Personalizer();
	private final ModelMapper modelMapper;

	@Transactional
	public RdgDevolverModel createOrGet(RdgDevolverDto.Request request, RdgModel rdg) {

		String token = UUID.randomUUID().toString();

		RdgDevolverModel devolver = this.rdgDevolverDao.findByIdByToken(request.getIdDevolver(), request.getToken())
				.orElseGet(() -> {
					RdgDevolverModel nuevo = new RdgDevolverModel();
					nuevo.setToken("nuevo");
					return nuevo;
				});

		if (devolver.getToken().equals("nuevo")) {
			// nuevo
			devolver.setArchivo(request.getArchivo());
			devolver.setRdg(rdg);
			devolver.setDetalle(request.getDetalle());
			devolver.setDocumento(request.getDocumento());
			devolver.setMoneda(request.getMoneda());
			devolver.setFecha(this.personalizer.getFecha(request.getFecha()));
			devolver.setImporte(request.getImporte());
			devolver.setGasto(request.getGasto());
			devolver.setDeposito(request.getDeposito());
			devolver.setNumero(request.getNumero());
			devolver.setAgregado(Estado.SI);
			devolver.setToken(token);
			if (request.getAgregado() == Estado.SI)
				this.rdgDevolverDao.save(devolver);
		} else {
			// actualizar
			devolver.setArchivo(request.getArchivo());
			devolver.setDetalle(request.getDetalle());
			devolver.setDocumento(request.getDocumento());
			devolver.setFecha(this.personalizer.getFecha(request.getFecha()));
			devolver.setGasto(request.getGasto());
			devolver.setDeposito(request.getDeposito());
			devolver.setNumero(request.getNumero());
			devolver.setAgregado(request.getAgregado());
			this.rdgDevolverDao.save(devolver);
		}

		return devolver;
	}
	@Transactional
	public List<RdgDevolverDto.Response> findByIdRdgByAgregadoResponse(Long idRdg,Estado agregado) {
		List<RdgDevolverDto.Response> lista = this.rdgDevolverDao.findByIdRdgByAgregado(idRdg,agregado.name()).stream()
				.map(devolver -> this.modelToResponse(devolver)).collect(Collectors.toList());
		return lista;
	}
	
	private RdgDevolverDto.Response modelToResponse(RdgDevolverModel devolver) {
		return this.modelMapper.map(devolver, RdgDevolverDto.Response.class);
	}
	/*
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

	
	private CuentaDto.Response modelToResponseCuenta(CuentaModel cuenta) {
		return this.modelMapper.map(cuenta, CuentaDto.Response.class);
	}
*/
}
