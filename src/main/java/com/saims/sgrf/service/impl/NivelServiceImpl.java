package com.saims.sgrf.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.sgrf.dao.NivelDao;
import com.saims.sgrf.model.AreaModel;
import com.saims.sgrf.model.NivelModel;
import com.saims.sgrf.service.NivelService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NivelServiceImpl implements NivelService{
	private final NivelDao nivelDao;
	private final ModelMapper modelMapper;
	
	@Transactional
	public NivelModel createOrGet(AreaModel padre,AreaModel hijo) {
		NivelModel nivel = this.nivelDao.findByIdPadreByIdHijo(padre.getId(),hijo.getId())
				.orElseGet(()->{
					NivelModel nuevo = new NivelModel();
					nuevo.setPadre(padre);
					nuevo.setHijo(hijo);
					this.nivelDao.save(nuevo);
					return nuevo;
				});
		
		return nivel;
	}
	
	@Transactional
	public NivelModel findByIdHijo(AreaModel hijo) {
		NivelModel nivel = this.nivelDao.findByIdHijo(hijo.getId())
				.orElseGet(()->{
					NivelModel nuevo = new NivelModel();
					nuevo.setId(0L);
					return nuevo;
				});
		return nivel;
	}
	
	@Transactional
	public List<NivelModel> findAll() {
		return this.nivelDao.findAll();
	}

}
