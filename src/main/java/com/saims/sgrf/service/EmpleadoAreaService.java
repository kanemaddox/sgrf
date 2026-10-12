package com.saims.sgrf.service;

import com.saims.sgrf.model.AreaModel;
import com.saims.sgrf.model.EmpleadoAreaModel;
import com.saims.sgrf.model.EmpleadoModel;

public interface EmpleadoAreaService {
	EmpleadoAreaModel createOrGet (EmpleadoModel empleado, AreaModel area);
	EmpleadoAreaModel getEmpleado (Long IdEmpleado);
}
