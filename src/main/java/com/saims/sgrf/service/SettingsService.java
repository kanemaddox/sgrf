package com.saims.sgrf.service;

import com.saims.sgrf.dto.SettingsDto;

public interface SettingsService {
	SettingsDto.Response getValue(Long id);
	SettingsDto.Response updateSettings(SettingsDto.Response response);
	SettingsDto.Response createSettings(SettingsDto.Request request);

}
