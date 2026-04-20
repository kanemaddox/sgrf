package com.saims.sgrf.model;

import com.saims.sgrf.base.StatusEntity;
import com.saims.sgrf.enums.TipoEmpleado;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

/**
 * 📌 Clase: EmpleadoModel
 * 
 * 🧾 Descripción:
 * Representa un empleado dentro del sistema ERP.
 * Contiene relaciones con Persona, Cargo, Sucursal y Profesión.
 * Permite definir la jerarquía organizacional mediante el tipo de empleado.
 * 
 */

@Entity
@Table(name = "empleado")
public class EmpleadoModel extends StatusEntity{

    // 🔗 Relación con Persona
    @ManyToOne
    @JoinColumn(name = "id_persona", nullable = false)
    private PersonaModel persona;

    // 🔗 Relación con Cargo
    @ManyToOne
    @JoinColumn(name = "id_cargo", nullable = false)
    private CargoModel cargo;

    // 🔗 Relación con Sucursal
    @ManyToOne
    @JoinColumn(name = "id_sucursal", nullable = false)
    private SucursalModel sucursal;
    
    // numero de identificacion del jefe
    @Column(name = "idp_jefe", nullable = false)
    private String idpJefe;
    

    // 🧠 Tipo de empleado (mejor que int )
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private TipoEmpleado tipo;
}