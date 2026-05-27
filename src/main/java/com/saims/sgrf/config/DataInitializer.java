package com.saims.sgrf.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.saims.sgrf.dao.PersonaDao;
import com.saims.sgrf.dao.SettingsDao;
import com.saims.sgrf.dao.UsuarioDao;
import com.saims.sgrf.enums.Rol;
import com.saims.sgrf.model.PersonaModel;
import com.saims.sgrf.model.SettingsModel;
import com.saims.sgrf.model.UsuarioModel;

import lombok.Data;

@Data
@Configuration
public class DataInitializer {
	private final PersonaDao personaDao;
	private final SettingsDao settingsDao;
	private Personalizer personalizer = new Personalizer();

    @Bean
    CommandLineRunner initAdmin(UsuarioDao usuario) {
        return args -> {
            if (usuario.findByUsuario("admin").isEmpty()) {
            	
            	Map<String, Object> local = new HashMap<>();
        		local.put("idp", "");
        		
            	Map<String, Object> value = new HashMap<>();
        		value.put("titulo", "Titulo");
        		value.put("seccionUno", "Titulo Seccion I");
        		value.put("seccionDos", "Titulo Seccion II");
        		value.put("seccionTres", "Titulo Seccion II");
        		value.put("detalle", "Detalle Seccion III");
        		value.put("estadoTres", false);
        		
        		
        		Map<String, Object> pernicions = new HashMap<>();
        		pernicions.put("menuAdmin",true);
        		pernicions.put("menuSettings",true);
        		pernicions.put("mi_cuenta",true);
        		pernicions.put("perfil",true);
        		pernicions.put("cuenta_bancaria",true);       		
            	
            	PersonaModel persona = new PersonaModel();
            	persona.setNombre("Admin");
            	persona.setIdp("admin");
            	persona.setEmail("admin@sgrf.com");
            	persona.setFoto("usuario.png");
            	this.personaDao.save(persona);
            	
                UsuarioModel admin = new UsuarioModel();
                admin.setEstado(true);
                admin.setIntentos(0);
                admin.setUsuario(persona.getIdp());
                admin.setPassword(this.personalizer.password("admin"));
                admin.setRol(Rol.ADMIN);
                admin.setPersona(persona);
                admin.setPernicions(pernicions);
        		
        		SettingsModel setting = new SettingsModel();
        		setting.setCategoria("Configuracion");
        		setting.setNombre("Key_Local");        		
        		setting.setValue(local);
        		
        		this.settingsDao.save(setting);
        		
        		setting = new SettingsModel();
        		setting.setCategoria("Formulario");
        		setting.setNombre("Key_FSR");        		
        		setting.setValue(value);
        		
        		this.settingsDao.save(setting);
        		
        		usuario.save(admin);
                
            }
        };
    }
}