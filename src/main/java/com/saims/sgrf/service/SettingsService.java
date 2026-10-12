package com.saims.sgrf.service;

import com.saims.sgrf.dto.SettingsDto;
import com.saims.sgrf.model.SettingsModel;

public interface SettingsService {
	SettingsDto.Response getValue(Long id);
	SettingsDto.Response updateSettings(SettingsDto.Response response);
	SettingsDto.Response createSettings(SettingsDto.Request request);
	SettingsModel getValueModel(Long id);

}
