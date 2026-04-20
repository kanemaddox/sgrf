package com.saims.sgrf.model;


import com.saims.sgrf.base.BaseEntity;

// Importaciones de JPA
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
// Importaciones de validación
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
// Importaciones de Lombok
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 📌 Clase: PersonaModel
 * 
 * 🧾 Descripción:
 * Representa la entidad "persona" en la base de datos.
 * Almacena la información básica de una persona dentro del sistema ERP.
 * 
 * 🚀 Mejoras implementadas:
 * ✔ Validaciones (Bean Validation)
 * ✔ Restricciones en base de datos
 * ✔ Auditoría (fechas automáticas)
 * ✔ Preparada para escalabilidad
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "persona")
public class PersonaModel extends BaseEntity{

    /**
     * 🪪 Documento de identidad (CI / RUT)
     */
    @NotBlank(message = "El documento de identidad es obligatorio")
    @Size(min = 5, max = 20)
    @Column(name = "idp", nullable = false, length = 20, unique = true)
    private String idp;

    /**
     * 👤 Nombre de la persona
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /**
     * 👤 Apellido paterno
     */
    @Size(max = 100)
    @Column(name = "paterno",nullable = true, length = 100)
    private String paterno;

    /**
     * 👤 Apellido materno
     */
    @Size(max = 100)
    @Column(name = "materno",nullable = true, length = 100)
    private String materno;

    /**
     * 📧 Correo electrónico
     */
    @Email(message = "Debe ser un correo electrónico válido")
    @Size(max = 150)
    @Column(name = "email", length = 150, unique = true)
    private String email;
    
    
    /**
     * 📧 fecha de nacimiento
     */
    @Column(name = "fecha_nacimineto",nullable = true)
    private String fechaNacimiento;
    
    /**
     * 📧 sexo
     */
    @Column(name = "sexo", nullable = true)
    private Boolean sexo;
    
    /**
     * 📧 celular
     */
    @Column(name = "celular", nullable = true)
    private String celular;
    
    
}