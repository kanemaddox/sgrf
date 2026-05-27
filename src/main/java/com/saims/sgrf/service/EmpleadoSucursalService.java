package com.saims.sgrf.service;

import com.saims.sgrf.model.AreaModel;
import com.saims.sgrf.model.CentroCostosModel;
import com.saims.sgrf.model.EmpleadoModel;
import com.saims.sgrf.model.EmpleadoSucursalModel;
import com.saims.sgrf.model.SucursalModel;

public interface EmpleadoSucursalService {
	EmpleadoSucursalModel addEmpleadoSucursal(EmpleadoModel empleado, SucursalModel sucursal,AreaModel area,CentroCostosModel centro);
	EmpleadoSucursalModel getSucursalActivo(Long idEmpleado);

}
