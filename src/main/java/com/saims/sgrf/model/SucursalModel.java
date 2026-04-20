package com.saims.sgrf.model;

import com.saims.sgrf.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
// (Opcional pero recomendado)
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 📌 Clase: SucursalModel
 * 
 * 🧾 Descripción:
 * Representa una sucursal dentro del sistema ERP.
 * Contiene información básica como código, nombre y prefijo,
 * permitiendo su identificación y gestión en la base de datos.
 * 
 * 🗄️ Tabla: sucursal
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sucursal")
public class SucursalModel extends BaseEntity{

    /**
     * 🏷️ Prefijo de la sucursal (ej: LPZ, SCZ)
     */
    @NotBlank(message = "El prefijo es obligatorio")
    @Size(max = 10)
    @Column(name = "prefijo", nullable = false, length = 10)
    private String prefijo;

    /**
     * 🔢 Código interno de la sucursal
     */
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 20)
    @Column(name = "codigo", nullable = false, unique = true, length = 20)
    private String codigo;

    /**
     * 🏢 Nombre de la sucursal
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
}
