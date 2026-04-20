package com.saims.sgrf.base;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) 
public abstract class BaseEntity {
	
	/**
     * 🆔 Identificador único del área
     * - Se genera automáticamente
     * - No puede ser nulo
     */
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    protected Long id;

	/**
     * 📅 Fecha de creación (automática)
     */
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @org.springframework.data.annotation.CreatedDate
    protected LocalDateTime fechaCreacion;

    /**
     * 🔄 Fecha de última actualización
     */
    @Column(name = "fecha_actualizacion")
    @Temporal(TemporalType.TIMESTAMP)
    @org.springframework.data.annotation.LastModifiedDate
    protected LocalDateTime fechaActualizacion;

}