package com.saims.sgrf.model;

import com.saims.sgrf.base.StatusEntity;

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
 * 📌 Clase: BancoModel
 * 
 * 🧾 Descripción:
 * Representa la entidad "banco" en la base de datos.
 * Esta clase permite almacenar los bancos disponibles en el sistema ERP,
 * los cuales pueden ser utilizados en módulos como pagos, cuentas bancarias
 * o transacciones financieras.
 * 
 * 🏦 Ejemplos:
 * - Banco Unión
 * - Banco Mercantil Santa Cruz
 * - BNB (Banco Nacional de Bolivia)
 * 
 * 🔗 Esta clase está mapeada a la tabla "banco" mediante JPA.
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
@Table(name = "banco")
public class BancoModel extends StatusEntity{

    /**
     * 🏦 Nombre del banco
     * 
     * - Representa el nombre de la entidad financiera
     * - Ejemplo: "Banco Unión"
     * - Se recomienda aplicar restricciones para evitar duplicados
     */
    @Column(name = "nombre", nullable = false)
    private String nombre;

}