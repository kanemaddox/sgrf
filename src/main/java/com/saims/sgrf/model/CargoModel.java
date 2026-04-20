package com.saims.sgrf.model;

import com.saims.sgrf.base.StatusEntity;

// Importaciones de JPA (persistencia)
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
// Importaciones de Lombok para reducir código
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 📌 Clase: CargoModel
 * 
 * 🧾 Descripción:
 * Representa la entidad "cargo" en la base de datos.
 * Un cargo define el rol o puesto de un usuario dentro del sistema ERPM.
 * 
 * 🗃️ Ejemplos de cargos:
 * - Administrador
 * - Contador
 * - Supervisor
 * 
 * 🔗 Esta clase está mapeada a la tabla "cargo" usando JPA.
 */

// Lombok: genera automáticamente los métodos getter y setter
@Getter
@Setter

// Lombok: constructor con todos los atributos
@AllArgsConstructor

// Lombok: constructor vacío (requerido por JPA)
@NoArgsConstructor

// Indica que esta clase es una entidad persistente
@Entity

// Define el nombre de la tabla en la base de datos
@Table(name = "cargo")
public class CargoModel extends StatusEntity{

    
    /**
     * 📛 Nombre del cargo
     * 
     * - Representa el nombre del rol dentro del sistema
     * - Ejemplo: "Administrador", "Gerente", etc.
     * - Puede configurarse con restricciones adicionales (nullable, length)
     */
    @Column(name = "nombre", nullable = false)
    private String nombre;

}
