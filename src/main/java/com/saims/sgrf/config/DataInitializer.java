package com.saims.sgrf.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.saims.sgrf.dao.PersonaDao;
import com.saims.sgrf.dao.UsuarioDao;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.model.UsuarioModel;

import lombok.Data;

@Data
@Configuration
public class DataInitializer {
	private final PersonaDao personaDao;
	private Personalizer personalizer = new Personalizer();

    @Bean
    CommandLineRunner initAdmin(UsuarioDao usuario) {
        return args -> {
            if (usuario.findByUsuario("admin").isEmpty()) {
            	PersonaModel persona = new PersonaModel();
            	persona.setNombre("Admin");
            	persona.setIdp("admin");
            	persona.setEmail("admin@sgrf.com");
            	personaDao.save(persona);
            	
                UsuarioModel admin = new UsuarioModel();
                admin.setEstado(true);
                admin.setUsuario(persona.getIdp());
                admin.setPassword(this.personalizer.password("admin"));
                admin.setImagen("usuario.png");
                admin.setPersona(persona);
                
                usuario.save(admin);
            }
        };
    }
}