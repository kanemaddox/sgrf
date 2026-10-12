package com.saims.sgrf.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.saims.sgrf.base.BaseEntity;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.enums.Moneda;

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
@Table(name = "rdg_devolver")
public class RdgDevolverModel extends BaseEntity{
	
	@Column(name = "fecha")
	private LocalDateTime fecha;
	
	@Column(name = "numero")
	private String numero;
	
	@Column(name = "detalle", columnDefinition = "LONGTEXT")
	private String detalle;
	
	@Column(name = "importe",precision = 10, scale = 2)
	private BigDecimal importe;
		
	@Column(name = "gasto",precision = 10, scale = 2)
	private BigDecimal gasto;
	
	@Column(name = "deposito",precision = 10, scale = 2)
	private BigDecimal deposito;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull
	private Moneda moneda;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull
	private Estado agregado;
	
	@Column(name = "documento")
	private String documento;
	
	@Column(name = "archivo")
	private String archivo;
	
	@Column(name = "token")
	private String token;
	
	@ManyToOne
    @JoinColumn(name = "id_rdg")
    private RdgModel rdg;
}
