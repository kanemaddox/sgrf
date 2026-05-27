package com.saims.sgrf.model;

import com.saims.sgrf.base.BaseEntity;
import com.saims.sgrf.enums.Estado;

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

@Entity
@Table(name = "aprobacion")
public class AprobacionModel extends BaseEntity{
	
	@Column(name="tipo")
    private String tipo;
	
	@Column(name="destino")
    private int destino;
	
	@Column(name="obs")
    private String obs;
	
	@ManyToOne
    @JoinColumn(name = "id_srf")
    private SrfModel srf;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull
	private Estado estado;
	
	@ManyToOne
	@JoinColumn(name = "id_persona")
	private PersonaModel persona;

}
