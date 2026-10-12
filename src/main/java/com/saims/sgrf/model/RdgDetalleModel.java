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
@Table(name = "rdg_detalle")
public class RdgDetalleModel extends BaseEntity{
	
	@Column(name = "fecha")
	private LocalDateTime fecha;
	
	@Column(name = "numero")
	private String numero;
	
	@Column(name = "detalle", columnDefinition = "LONGTEXT")
	private String detalle;
	
	@Column(name = "importe",precision = 10, scale = 2)
	private BigDecimal importe;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull
	private Moneda moneda;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull
	private Estado agregado;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull
	private Estado estado;
	
	@Column(name = "documento")
	private String documento;
	
	@Column(name = "archivo")
	private String archivo;
	
	@Column(name = "token")
	private String token;
	
	
	@JdbcTypeCode(SqlTypes.JSON)
    @Column(name="aprobado")
    private Map<String, Object> aprobado;
	
	@ManyToOne
    @JoinColumn(name = "id_cuenta")
    private CuentaModel cuenta;
	
	@ManyToOne
    @JoinColumn(name = "id_rdg")
    private RdgModel rdg;

}
