package com.saims.sgrf.model;

import com.saims.sgrf.base.StatusEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "usuario")
public class UsuarioModel extends StatusEntity{
	
	/**
     * 👤 Nombre de la persona
     */
    @NotBlank(message = "El nombre de usuariio es obligatorio")
    @Size(max = 100)
    @Column(name = "usuario", nullable = false, length = 100)
    private String usuario;
    
    /**
     * 👤 password del usuario
     */
    @NotBlank(message = "El nombre de usuariio es obligatorio")
    @Column(name = "password", nullable = false)
    private String password;
    
    
    /**
     * 🏦 Relación con persona
     */
    @OneToOne
    @JoinColumn(name = "id_persona", nullable = false)
    private PersonaModel persona;
}
