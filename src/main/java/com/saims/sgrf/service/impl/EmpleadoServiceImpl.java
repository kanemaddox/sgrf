package com.saims.sgrf.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.saims.sgrf.dao.CargoDao;
import com.saims.sgrf.dao.EmpleadoDao;
import com.saims.sgrf.dao.PersonaDao;
import com.saims.sgrf.dao.SucursalDao;
import com.saims.sgrf.dto.DatosDtoRequest;
import com.saims.sgrf.dto.EmpleadoDtoRequest;
import com.saims.sgrf.dto.EmpleadoDtoResponse;
import com.saims.sgrf.enums.TipoEmpleado;
import com.saims.sgrf.model.CargoModel;
import com.saims.sgrf.model.EmpleadoModel;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.model.SucursalModel;
import com.saims.sgrf.service.EmpleadoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoDao empleadoDao;
    private final ModelMapper modelMapper;
    private final PersonaDao personaDao;
    private final CargoDao cargoDao;
    private final SucursalDao sucursalDao;

    /**
     * Método principal para registrar o recuperar un empleado.
     * 
     * Flujo:
     * 1. Verifica si ya existe un empleado asociado a la persona.
     * 2. Si existe:
     *    - Retorna el empleado existente (opcionalmente se podría actualizar).
     * 3. Si no existe:
     *    - Crea un nuevo empleado con los datos proporcionados.
     * 
     * @param personaModel   Persona asociada al empleado
     * @param cargoModel     Cargo del empleado
     * @param profesionModel Profesión del empleado
     * @param sucursalModel  Sucursal donde trabaja
     * @param datosDtoRequest Datos adicionales
     * @return EmpleadoModel persistido o existente
     */
    @Transactional
    public EmpleadoModel addDatos(PersonaModel personaModel,CargoModel cargoModel,SucursalModel sucursalModel,DatosDtoRequest datosDtoRequest) {

        return this.empleadoDao.findByPersona_Id(personaModel.getId())
                .map(empleadoExistente -> actualizarEmpleado(empleadoExistente, cargoModel, sucursalModel, datosDtoRequest))
                .orElseGet(() -> crearEmpleado(personaModel, cargoModel, sucursalModel, datosDtoRequest));
    }
    
    @Transactional
    public EmpleadoDtoResponse create(EmpleadoDtoRequest request) {
    	PersonaModel persona = this.personaDao.getId(request.getIdPersona());
    	CargoModel cargo = this.cargoDao.getId(request.getIdCargo());
    	SucursalModel sucursal = this.sucursalDao.getId(request.getIdSucursal());
    	
    	EmpleadoModel empleado = new EmpleadoModel();
    	empleado.setCargo(cargo);
    	empleado.setPersona(persona);
    	empleado.setSucursal(sucursal);
    	empleado.setEstado(request.getEstado());
    	empleado.setTipo(request.getTipoEmpleado());
    	this.empleadoDao.save(empleado);
    	return this.modelToResponse(empleado);
    }

    /**
     * Crea un nuevo empleado con valores por defecto.
     */
    private EmpleadoModel crearEmpleado(PersonaModel personaModel,CargoModel cargoModel,SucursalModel sucursalModel,DatosDtoRequest datosDtoRequest) {

        EmpleadoModel nuevo = new EmpleadoModel();
        nuevo.setPersona(personaModel);
        nuevo.setCargo(cargoModel);
        nuevo.setSucursal(sucursalModel);
        nuevo.setEstado(true);
        nuevo.setIdpJefe(datosDtoRequest.getIdpJefe());
        nuevo.setTipo(TipoEmpleado.SUBORDINADO);

        return empleadoDao.save(nuevo);
    }

    /**
     * Actualiza los datos de un empleado existente.
     */
    private EmpleadoModel actualizarEmpleado(EmpleadoModel empleado,CargoModel cargoModel,SucursalModel sucursalModel,DatosDtoRequest datosDtoRequest) {

        empleado.setCargo(cargoModel);
        empleado.setSucursal(sucursalModel);
        empleado.setIdpJefe(datosDtoRequest.getIdpJefe());
        empleado.setEstado(true);

        return this.empleadoDao.save(empleado);
    }

    /**
     * Obtiene un empleado por ID.
     */
    @Transactional
    public EmpleadoDtoResponse getById(Long id) {
        EmpleadoModel empleado = empleadoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));

        return this.modelToResponse(empleado);
    }

    /**
     * Lista todos los empleados Response.
     */
    @Transactional
    public List<EmpleadoDtoResponse> getAll() {
        return empleadoDao.findAll()
                .stream()
                .map(emp -> this.modelToResponse(emp))
                .toList();
    }
    
    /**
     * Lista todos los empleados Response.
     */
    @Transactional
    public List<EmpleadoModel> findAll() {
        return empleadoDao.findAll();
    }

    /**
     * Lista empleados paginados.
     */
    @Transactional
    public Page<EmpleadoDtoResponse> getAllPaginated(Pageable pageable) {
        return empleadoDao.findAll(pageable)
                .map(emp -> this.modelToResponse(emp));
    }

    /**
     * Busca un empleado por ID de persona.
     */
    @Transactional
    public EmpleadoDtoResponse findByPersonaId(Long personaId) {
        EmpleadoModel empleado = empleadoDao.findByPersona_Id(personaId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado para la persona"));

        return this.modelToResponse(empleado);
    }

    /**
     * Cambia el estado (activo/inactivo) de un empleado.
     */
    @Transactional
    public void cambiarEstado(Long id, boolean estado) {
        EmpleadoModel empleado = this.empleadoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        empleado.setEstado(estado);
        this.empleadoDao.save(empleado);
    }
    
    @Transactional
    public void setAllEstado(boolean estado) {
        List<EmpleadoModel> empleados = this.empleadoDao.findAll();
        empleados.forEach(e->{
        	e.setEstado(estado);
        });
        
        this.empleadoDao.saveAll(empleados);
        
    }

    /**
     * Elimina un empleado (borrado lógico recomendado).
     */
    @Transactional
    public void delete(Long id) {
        EmpleadoModel empleado = this.empleadoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        empleado.setEstado(false); // borrado lógico
        this.empleadoDao.save(empleado);
    }
    
    /**
     * 📌 Convierte Entity → DTO
     */
    private EmpleadoDtoResponse modelToResponse(EmpleadoModel empleado) {
        return this.modelMapper.map(empleado, EmpleadoDtoResponse.class);
    }
}