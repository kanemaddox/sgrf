package com.saims.sgrf.service;

import java.util.List;

import com.saims.sgrf.model.AreaModel;
import com.saims.sgrf.model.NivelModel;

public interface NivelService {
	NivelModel createOrGet(AreaModel padre,AreaModel hijo);
	NivelModel findByIdHijo(AreaModel hijo);
	List<NivelModel> findAll();

}
