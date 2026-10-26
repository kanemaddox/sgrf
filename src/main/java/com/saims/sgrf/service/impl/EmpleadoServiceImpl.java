package com.saims.sgrf.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.EmpleadoDao;
import com.saims.sgrf.dao.PersonaDao;
import com.saims.sgrf.dto.DatosDto;
import com.saims.sgrf.dto.EmpleadoDto;
import com.saims.sgrf.dto.PersonaDto;
import com.saims.sgrf.enums.TipoEmpleado;
import com.saims.sgrf.model.EmpleadoModel;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.service.EmpleadoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements EmpleadoService {

	private final EmpleadoDao empleadoDao;
	private final ModelMapper modelMapper;
	private final PersonaDao personaDao;
	private Personalizer personaizer = new Personalizer();

	/**
	 * Método principal para registrar o recuperar un empleado.
	 * 
	 * Flujo: 1. Verifica si ya existe un empleado asociado a la persona. 2. Si
	 * existe: - Retorna el empleado existente (opcionalmente se podría actualizar).
	 * 3. Si no existe: - Crea un nuevo empleado con los datos proporcionados.
	 * 
	 * @param personaModel    Persona asociada al empleado
	 * @param cargoModel      Cargo del empleado
	 * @param profesionModel  Profesión del empleado
	 * @param sucursalModel   Sucursal donde trabaja
	 * @param datosDtoRequest Datos adicionales
	 * @return EmpleadoModel persistido o existente
	 */
	@Transactional
	public EmpleadoModel addDatos(PersonaModel personaModel, DatosDto.Request request) {
		EmpleadoModel empleado = this.empleadoDao.findByPersona_Id(personaModel.getId()).map(e -> {
			e.setTipo(request.getTipo() ? TipoEmpleado.JEFE : TipoEmpleado.SUBORDINADO);
			e.setEstado(true);
			this.empleadoDao.save(e);
			return e;

		}).orElseGet(() -> {
			EmpleadoModel nuevo = new EmpleadoModel();
			nuevo.setPersona(personaModel);
			nuevo.setTipo(request.getTipo() ? TipoEmpleado.JEFE : TipoEmpleado.SUBORDINADO);
			nuevo.setEmail(this.personaizer.normalizerLower(request.getEmailEmpleado()));
			nuevo.setEstado(true);
			return empleadoDao.save(nuevo);
		});

		return empleado;
	}

	@Transactional
	public EmpleadoDto.Response create(EmpleadoDto.Request request) {
		PersonaModel persona = this.personaDao.getId(request.getIdPersona());

		EmpleadoModel empleado = new EmpleadoModel();
		empleado.setPersona(persona);
		empleado.setEstado(request.getEstado());
		empleado.setTipo(request.getTipoEmpleado());
		this.empleadoDao.save(empleado);
		return this.modelToResponse(empleado);
	}
	
	@Transactional
	public EmpleadoDto.Response updateCorreo(PersonaDto.Response response) {
		EmpleadoModel empleado = this.empleadoDao.getEmpleado(response.getId());
		empleado.setEmail(response.getEmail());
		this.empleadoDao.save(empleado);
		return this.modelToResponse(empleado);
	}

	/**
	 * Crea un nuevo empleado con valores por defecto.
	 */
	private EmpleadoModel crearEmpleado(PersonaModel personaModel, DatosDto.Request request) {

		EmpleadoModel nuevo = new EmpleadoModel();
		nuevo.setPersona(personaModel);
		nuevo.setEstado(true);
		if (request.getTipo())
			nuevo.setTipo(TipoEmpleado.JEFE);
		else
			nuevo.setTipo(TipoEmpleado.SUBORDINADO);

		return empleadoDao.save(nuevo);
	}

	/**
	 * Obtiene un empleado por ID.
	 */
	@Transactional
	public EmpleadoDto.Response getById(Long id) {
		EmpleadoModel empleado = empleadoDao.findById(id)
				.orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));

		return this.modelToResponse(empleado);
	}

	/**
	 * Lista todos los empleados Response.
	 */
	@Transactional
	public List<EmpleadoDto.Response> getAll() {
		return empleadoDao.findAll().stream().map(emp -> this.modelToResponse(emp)).toList();
	}

	@Transactional
	public List<EmpleadoModel> findByTipo(TipoEmpleado tipo) {
		List<EmpleadoModel> empleado = this.empleadoDao.findByTipo(tipo);
		return empleado;
	}

	@Transactional
	public List<EmpleadoDto.Response> findByTipoResponse(TipoEmpleado tipo) {
		List<EmpleadoDto.Response> empleados = this.empleadoDao.findByTipo(tipo).stream()
				.map(empleado -> this.modelToResponse(empleado)).toList();
		return empleados;
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
	public Page<EmpleadoDto.Response> getAllPaginated(Pageable pageable) {
		return empleadoDao.findAll(pageable).map(emp -> this.modelToResponse(emp));
	}

	/**
	 * Busca un empleado por ID de persona.
	 */
	@Transactional
	public EmpleadoDto.Response findByPersonaId(Long personaId) {
		EmpleadoModel empleado = empleadoDao.findByPersona_Id(personaId).orElseGet(() -> {
			EmpleadoModel nuevo = new EmpleadoModel();
			nuevo.setId(0L);
			return nuevo;
		});

		return this.modelToResponse(empleado);
	}

	public EmpleadoModel findByPersonaIdModel(Long personaId) {
		EmpleadoModel empleado = empleadoDao.findByPersona_Id(personaId).orElseGet(() -> {
			EmpleadoModel nuevo = new EmpleadoModel();
			nuevo.setId(0L);
			return nuevo;
		});

		return empleado;
	}

	@Transactional
	public List<EmpleadoModel> findByEstado(boolean estado) {
		List<EmpleadoModel> empleados = this.empleadoDao.findByEstado(estado);
		return empleados;
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
		empleados.forEach(e -> {
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
	private EmpleadoDto.Response modelToResponse(EmpleadoModel empleado) {
		return this.modelMapper.map(empleado, EmpleadoDto.Response.class);
	}

}