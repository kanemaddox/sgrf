package com.saims.sgrf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.DepartamentoDao;
import com.saims.sgrf.dto.DepartamentoDto;
import com.saims.sgrf.model.DepartamentoModel;
import com.saims.sgrf.service.DepartamentoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartamentoServiceImpl implements DepartamentoService {

	private final ModelMapper modelMapper;
	private Personalizer personalizer = new Personalizer();
	private final DepartamentoDao departamentoDao;

	@Transactional
	public DepartamentoModel createOrGet(String Nombre) {

		String dep = this.personalizer.normalizer(Nombre);
		return this.departamentoDao.findByNombre(dep)
				.orElseGet(() -> {
					DepartamentoModel nuevo = new DepartamentoModel();
					nuevo.setNombre(dep);
					return this.departamentoDao.save(nuevo);
				});
	}
	@Transactional
	public List<DepartamentoDto.Response> findByAll(){
		List<DepartamentoModel> model = this.departamentoDao.findAll();
		List<DepartamentoDto.Response> departamento = new ArrayList<DepartamentoDto.Response>();
		DepartamentoDto.Response item = null;
		for(DepartamentoModel value : model) {
			item = new DepartamentoDto.Response();
			item.setId(value.getId());
			item.setDepartamento(value.getNombre());
			departamento.add(item);
		}
		
		return departamento;
	}
	
	private DepartamentoDto.Response modelToResponse(DepartamentoModel departamento) {
		return (this.modelMapper.map(departamento, DepartamentoDto.Response.class));
	}
}
