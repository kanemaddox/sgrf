package com.saims.sgrf.model;

import com.saims.sgrf.base.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter // Genera getters automáticamente
@Setter // Genera setters automáticamente
@AllArgsConstructor // Constructor con todos los atributos
@NoArgsConstructor  // Constructor vacío

@Entity // Indica que es una entidad JPA
@Table(name = "persona_profesion") // Nombre de la tabla en la BD
public class PersonaProfesionModel extends BaseEntity {
	/**
     * 👤 Relación con persona
     */
    @ManyToOne
    @JoinColumn(name = "id_persona", nullable = false)
    private PersonaModel persona;
    
    /**
     * 👤 Relación con Profesion
     */
    @ManyToOne
    @JoinColumn(name = "id_profesion", nullable = false)
    private ProfesionModel profesion;

}
