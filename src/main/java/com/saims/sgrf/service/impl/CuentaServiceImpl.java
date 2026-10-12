package com.saims.sgrf.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.CuentaDao;
import com.saims.sgrf.dto.CuentaDto;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.model.CuentaModel;
import com.saims.sgrf.service.CuentaService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService {
	private final CuentaDao cuentaDao;
	private Personalizer personaliser = new Personalizer();
	private final ModelMapper modelMapper;

	@Transactional
	public CuentaDto.Response createOrGet(CuentaDto.Request request) {

		String cuenta = this.personaliser.normalizer(request.getCuenta());
		String tipo = this.personaliser.normalizer(request.getTipo());

		CuentaModel cuentaModel = this.cuentaDao.findByCodigoByTipo(cuenta, tipo).orElseGet(() -> {
			CuentaModel nuevo = new CuentaModel();
			nuevo.setTipo(tipo);
			nuevo.setCuenta(cuenta);
			nuevo.setNombre(request.getNombre());
			nuevo.setGlosa(request.getGlosa());
			nuevo.setCategoria(request.getCategoria());
			return this.cuentaDao.save(nuevo);
		});

		return this.modelToResponse(cuentaModel);
	}

	@Transactional
	public List<CuentaDto.Response> getCuentas(Estado categoria) {
		List<CuentaDto.Response> cuentas = null;
		if(categoria == Estado.TODOS) {
			cuentas = this.cuentaDao.findAll().stream().map(cuenta -> this.modelToResponse(cuenta))
					.collect(Collectors.toList());
		}else {
			cuentas = this.cuentaDao.findByCategoria(categoria.name()).stream().map(cuenta -> this.modelToResponse(cuenta))
					.collect(Collectors.toList());
		}
		return cuentas;
	}

	@Transactional
	public List<CuentaModel> findAll() {
		List<CuentaModel> cuentas = this.cuentaDao.findAll();
		return cuentas;
	}

	@Transactional
	public CuentaDto.Response updateCuenta(CuentaDto.Response response) {

		CuentaModel cuenta = this.cuentaDao.findById(response.getIdCuenta()).orElseGet(() -> {
			CuentaModel nuevo = new CuentaModel();
			nuevo.setId(0L);
			return nuevo;
		});

		if (cuenta.getId().longValue() > 0L) {

			cuenta.setTipo(this.personaliser.normalizer(response.getTipo()));
			cuenta.setCuenta(this.personaliser.normalizer(response.getCuenta()));
			cuenta.setNombre(response.getNombre());
			cuenta.setGlosa(response.getGlosa());
			cuenta.setCategoria(response.getCategoria());
			this.cuentaDao.save(cuenta);
		}
		return response;
	}

	private CuentaDto.Response modelToResponse(CuentaModel cuenta) {
		return this.modelMapper.map(cuenta, CuentaDto.Response.class);
	}

}
