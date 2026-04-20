package com.saims.sgrf.model;

import com.saims.sgrf.base.StatusEntity;

// 📦 Importaciones JPA
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
// 📦 Validaciones (recomendado 🔥)
import jakarta.validation.constraints.NotBlank;
// 📦 Importaciones Lombok
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 📌 Clase: AreaModel
 * 
 * 🧾 Descripción:
 * Representa un área dentro del sistema ERP (ej: RRHH, Finanzas, TI).
 * 
 */
@Getter // Genera getters automáticamente
@Setter // Genera setters automáticamente
@AllArgsConstructor // Constructor con todos los atributos
@NoArgsConstructor  // Constructor vacío

@Entity // Indica que es una entidad JPA
@Table(name = "area") // Nombre de la tabla en la BD
public class AreaModel extends StatusEntity{

    /**
     * 📛 Nombre del área
     * - No puede estar vacío
     * - Máximo 100 caracteres
     */
    @NotBlank(message = "El nombre del área es obligatorio")
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;
    
}