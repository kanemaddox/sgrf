package com.saims.sgrf.service;

import java.util.List;

import com.saims.sgrf.dto.DatosDto;
import com.saims.sgrf.dto.EmpleadoDto;
import com.saims.sgrf.model.CargoModel;
import com.saims.sgrf.model.EmpleadoCargoModel;
import com.saims.sgrf.model.EmpleadoModel;

public interface EmpleadoCargoService {
	EmpleadoCargoModel addEmpleadoCargo(EmpleadoModel empleado, CargoModel cargo, DatosDto.Request request);
	EmpleadoCargoModel getCargoActivo(Long idEmplado);
	List<EmpleadoDto.Cargo> getEmpleadoCargoUsuario(Long idUsuario);
	List<EmpleadoCargoModel> findByEstado(boolean estado);

}
