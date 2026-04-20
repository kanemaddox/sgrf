package com.saims.sgrf.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.PersonaDao;
import com.saims.sgrf.dto.DatosDtoRequest;
import com.saims.sgrf.dto.PersonaDtoRequest;
import com.saims.sgrf.dto.PersonaDtoResponse;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.service.PersonaService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonaServiceImpl implements PersonaService {

    private final PersonaDao personaDao;
    private final ModelMapper modelMapper;
    private Personalizer personalizer = new Personalizer();
    /**
     * Crea una persona o la obtiene si ya existe mediante IDP
     * 
     * @param datosDtoRequest datos de entrada
     * @return PersonaModel persistida
     */
    @Transactional
    public PersonaModel createOrGet(DatosDtoRequest datosDtoRequest) {

    	String idp = this.personalizer.normalizerIdp(datosDtoRequest.getIdp());
        
        PersonaModel personaModel = personaDao.findByIdp(idp)
                .orElseGet(() -> {
                    PersonaModel nuevo = new PersonaModel();
                    nuevo.setIdp(idp);
                    nuevo.setNombre(this.personalizer.normalizerName(datosDtoRequest.getNombre()));
                    nuevo.setPaterno(this.personalizer.normalizerName(datosDtoRequest.getPaterno()));
                    nuevo.setMaterno(this.personalizer.normalizerName(datosDtoRequest.getMaterno()));
                    nuevo.setEmail(this.personalizer.normalizerLower(datosDtoRequest.getEmail()));
                    return personaDao.save(nuevo);
                });

        // Actualización de datos si cambian
        boolean updated = false;

        String email = this.personalizer.normalizerLower(datosDtoRequest.getEmail());

        if (email != null && !email.equals(personaModel.getEmail())) {
            personaModel.setEmail(email);
            updated = true;
        }

        if (updated) {
            this.personaDao.save(personaModel);
        }

        return personaModel;
    }

    /**
     * Crea una persona y retorna DTO de respuesta
     */
    public PersonaModel createPersona(PersonaDtoRequest request) {

        String idp = this.personalizer.normalizer(request.getIdp());

        return personaDao.findByIdp(idp)
                .orElseGet(() -> {
                    PersonaModel nuevo = new PersonaModel();
                    nuevo.setIdp(idp);
                    nuevo.setNombre(this.personalizer.normalizerName(request.getNombre()));
                    nuevo.setPaterno(this.personalizer.normalizerName(request.getPaterno()));
                    nuevo.setMaterno(this.personalizer.normalizerName(request.getMaterno()));
                    nuevo.setEmail(this.personalizer.normalizerLower(request.getEmail()));
                    return personaDao.save(nuevo);
                });
    }

    /**
     * Obtiene una persona por ID
     */
    @Transactional
    public PersonaDtoResponse getById(Long id) {
        PersonaModel persona = this.personaDao.getId(id);
        return this.modelToResponse(persona);
    }

    /**
     * Obtiene una persona por IDP
     */
    @Transactional
    public PersonaDtoResponse findByIdp(String idp) {

        PersonaModel persona = personaDao.findByIdp(this.personalizer.normalizer(idp))
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        return this.modelToResponse(persona);
    }

    /**
     * Lista todas las personas
     */
    @Transactional
    public List<PersonaDtoResponse> getAll() {

        return personaDao.findAll().stream()
                .map(persona -> this.modelToResponse(persona))
                .collect(Collectors.toList());
    }
    
    @Transactional
    public List<PersonaModel>findAll(){
    	return this.personaDao.findAll();
    }

    /**
     * Actualiza una persona
     */
    @Transactional
    public PersonaDtoResponse update(PersonaDtoRequest request) {

        PersonaModel persona = personaDao.findByIdp(this.personalizer.normalizer(request.getIdp()))
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        persona.setNombre(this.personalizer.normalizerName(request.getNombre()));
        persona.setPaterno(this.personalizer.normalizerName(request.getPaterno()));
        persona.setMaterno(this.personalizer.normalizerName(request.getMaterno()));
        persona.setEmail(this.personalizer.normalizerLower(request.getEmail()));
        persona.setFechaNacimiento(request.getFechaNacimiento());
        persona.setCelular(request.getCelular());
        persona.setSexo(request.getSexo());

        this.personaDao.save(persona);

        return this.modelToResponse(persona);
    }

    /**
     * Elimina una persona por ID
     */
    @Transactional
    public void delete(Long id) {
        this.personaDao.deleteById(id);
    }

    /**
     * Verifica existencia por IDP
     */
    @Transactional
    public boolean existsByIdp(String idp) {
        return this.personaDao.findByIdp(this.personalizer.normalizer(idp)).isPresent();
    }
    
    private PersonaDtoResponse modelToResponse(PersonaModel persona) {
    	return (this.modelMapper.map(persona, PersonaDtoResponse.class));
    }
    

}