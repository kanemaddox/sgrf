package com.saims.sgrf.model;

import com.saims.sgrf.base.StatusEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 📌 Clase: CentroCostosModel
 * 
 * 🧾 Descripción:
 * Representa un centro de costos dentro del sistema ERP.
 * Permite organizar y controlar los gastos asociados a una sucursal específica.
 * 
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "centrocostos")
public class CentroCostosModel extends StatusEntity{

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 20)
    @Column(name = "codigo", nullable = false, length = 20)
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Size(max = 20)
    @Column(name = "prefijo", nullable = false, length = 20)
    private String prefijo;

}