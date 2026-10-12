package com.saims.sgrf.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saims.sgrf.dao.SettingsDao;
import com.saims.sgrf.dto.SettingsDto;
import com.saims.sgrf.model.SettingsModel;
import com.saims.sgrf.service.SettingsService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {
	private final SettingsDao settingsDao;
	private final ObjectMapper objectMapper;
	private final ModelMapper modelMapper;

	@Transactional
	public SettingsDto.Response createSettings(SettingsDto.Request request) {
		SettingsModel model = new SettingsModel();
		model.setCategoria(request.getCategoria());
		model.setNombre(request.getNombre());
		model.setValue(request.getValue());
		this.settingsDao.save(model);
		return this.ModelToResponse(model);
	}

	@Transactional
	public SettingsDto.Response updateSettings(SettingsDto.Response response) {

		SettingsModel setting = this.settingsDao.findById(response.getId()).map(model -> {
			model.setValue(response.getValue());
			this.settingsDao.save(model);
			return model;
		}).orElseGet(() -> {
			SettingsModel nuevo = new SettingsModel();
			nuevo.setNombre("Error");
			return nuevo;
		});
		return this.ModelToResponse(setting);
	}

	@Transactional
	public SettingsDto.Response getValue(Long id) {
		SettingsModel model = this.settingsDao.findById(id).orElseGet(() -> {
			SettingsModel nuevo = new SettingsModel();
			nuevo.setNombre("Error");
			return nuevo;
		});
		return this.ModelToResponse(model);
	}
	@Transactional
	public SettingsModel getValueModel(Long id) {
		SettingsModel model = this.settingsDao.findById(id).orElseGet(() -> {
			SettingsModel nuevo = new SettingsModel();
			nuevo.setNombre("Error");
			return nuevo;
		});
		return model;
	}

	private SettingsDto.Response ModelToResponse(SettingsModel settings) {
		return this.modelMapper.map(settings, SettingsDto.Response.class);
	}

}
