package com.saims.sgrf.model;

import com.saims.sgrf.base.BaseEntity;

// Importaciones de JPA (persistencia)
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
// Importaciones de Lombok para reducir código repetitivo
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 📌 Clase: ProfesionModel
 * 
 * 🧾 Descripción:
 * Representa la entidad "profesion" en la base de datos.
 * Define las distintas profesiones que pueden estar asociadas a usuarios
 * o empleados dentro del sistema ERP.
 * 
 * 🗃️ Ejemplos de profesiones:
 * - Ingeniero
 * - Contador
 * - Abogado
 * - Médico
 * 
 * 🔗 Esta clase está mapeada a la tabla "profesion" mediante JPA.
 */

// Lombok: genera automáticamente getters y setters
@Getter
@Setter

// Lombok: constructor con todos los atributos
@AllArgsConstructor

// Lombok: constructor vacío (requerido por JPA)
@NoArgsConstructor

// Indica que esta clase es una entidad persistente
@Entity

// Define el nombre de la tabla en la base de datos
@Table(name = "profesion")
public class ProfesionModel extends BaseEntity{

    /**
     * 📛 Nombre de la profesión
     * 
     * - Representa la profesión del empleado
     * - Ejemplo: "Ingeniero", "Arquitecto", etc.
     * - Se recomienda definir restricciones para integridad de datos
     */
    @Column(name = "nombre", nullable = false)
    private String nombre;
}