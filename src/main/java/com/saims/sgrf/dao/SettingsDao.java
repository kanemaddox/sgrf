package com.saims.sgrf.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saims.sgrf.model.SettingsModel;

public interface SettingsDao extends JpaRepository<SettingsModel,Long>{

}
