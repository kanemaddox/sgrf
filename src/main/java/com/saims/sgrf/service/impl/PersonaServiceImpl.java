package com.saims.sgrf.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.saims.sgrf.config.Personalizer;
import com.saims.sgrf.dao.PersonaDao;
import com.saims.sgrf.dto.DatosDto;
import com.saims.sgrf.dto.PersonaDto;
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
    public PersonaModel createOrGet(DatosDto.Request request) {

    	String idp = this.personalizer.normalizerIdp(request.getIdp());
        PersonaModel personaModel = personaDao.findByIdp(idp)
        		.map(p ->{
        			String auxmail = p.getEmail();
        			String nuevo = this.personalizer.normalizerLower(request.getEmail()); 
        			if(!auxmail.equals(nuevo)) {
        				try {
            				p.setEmail(nuevo);
            				this.personaDao.save(p);
            			}catch(Exception e) {
            				p.setEmail(auxmail);
            			}	
        			}
        			return p;
        		})
                .orElseGet(() -> {
                    PersonaModel nuevo = new PersonaModel();
                    nuevo.setIdp(idp);
                    nuevo.setNombre(this.personalizer.normalizerName(request.getNombre()));
                    nuevo.setPaterno(this.personalizer.normalizerName(request.getPaterno()));
                    nuevo.setMaterno(this.personalizer.normalizerName(request.getMaterno()));
                    nuevo.setEmail(this.personalizer.normalizerLower(request.getEmail()));
                    nuevo.setFoto("usuario.png");
                    return personaDao.save(nuevo);
                });

        return personaModel;
    }

    /**
     * Crea una persona y retorna DTO de respuesta
     */
    public PersonaModel createPersona(PersonaDto.Request request) {

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
    public PersonaDto.Response getById(Long id) {
        PersonaModel persona = this.personaDao.getId(id);
        return this.modelToResponse(persona);
    }

    /**
     * Obtiene una persona por IDP
     */
    @Transactional
    public PersonaDto.Response findByIdp(String idp) {

        PersonaModel persona = personaDao.findByIdp(this.personalizer.normalizer(idp))
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        return this.modelToResponse(persona);
    }
    
    @Transactional
    public PersonaModel findByIdpModel(String idp) {
        PersonaModel persona = personaDao.findByIdp(this.personalizer.normalizer(idp))
        		.orElseGet(()->{
        			PersonaModel nuevo = new PersonaModel();
        			nuevo.setId(0L);
        			return nuevo;
        		});
        return persona;
    }

    /**
     * Lista todas las personas
     */
    @Transactional
    public List<PersonaDto.Response> getAll() {

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
    public PersonaDto.Response updateFull(PersonaDto.Response response) {

        PersonaModel persona = personaDao.findByIdp(this.personalizer.normalizer(response.getIdp()))
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        persona.setNombre(this.personalizer.normalizerName(response.getNombre()));
        persona.setPaterno(this.personalizer.normalizerName(response.getPaterno()));
        persona.setMaterno(this.personalizer.normalizerName(response.getMaterno()));
        persona.setEmail(this.personalizer.normalizerLower(response.getEmail()));
        persona.setFechaNacimiento(response.getFechaNacimiento());
        persona.setCelular(response.getCelular());
        persona.setSexo(response.getSexo());

        this.personaDao.save(persona);

        return this.modelToResponse(persona);
    }
    
    /**
     * Actualiza una persona
     */
    @Transactional
    public PersonaDto.Response updateCelular(PersonaDto.Response response) {

        PersonaModel persona = this.personaDao.getId(response.getId());
        persona.setCelular(response.getCelular());
        this.personaDao.save(persona);

        return this.modelToResponse(persona);
    }
    
    @Transactional
    public PersonaModel findByEmail(String email) {

        PersonaModel persona = this.personaDao.findByEmail(this.personalizer.normalizerLower(email))
        		.orElseGet(()->{
        			PersonaModel nuevo = new PersonaModel();
        			nuevo.setEmail("0");
        			return nuevo;
        		});
        return persona;
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
    
    private PersonaDto.Response modelToResponse(PersonaModel persona) {
    	return (this.modelMapper.map(persona, PersonaDto.Response.class));
    }
    

}