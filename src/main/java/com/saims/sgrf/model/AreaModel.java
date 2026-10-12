package com.saims.sgrf.model;

import com.saims.sgrf.base.StatusEntity;
import com.saims.sgrf.enums.TipoArea;

// 📦 Importaciones JPA
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
// 📦 Validaciones (recomendado 🔥)
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "area")
public class AreaModel extends StatusEntity{

	@NotBlank(message = "El código es obligatorio")
    @Size(max = 20)
    @Column(name = "codigo", nullable = false, length = 20)
    private String codigo;

    @Size(max = 20)
    @Column(name = "prefijo", nullable = false, length = 20)
    private String prefijo;
    
    @Column(name = "nivel")
    private int nivel;
    
    @ManyToOne
	@JoinColumn(name = "id_departamento", nullable = false)
	private DepartamentoModel departamento;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private TipoArea tipo;
    
}