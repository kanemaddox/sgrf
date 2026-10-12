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
@Table(name = "srf")
public class SrfModel extends BaseEntity {

	@Column(name = "numero")
	private String numero;
	
	@Column(name = "cargo")
	private String cargo;
	
	@Column(name = "centrocosto")
	private String centrocosto;
	
	@Column(name = "codigo")
	private String codigo;

	@Column(name = "gestion")
	private int gestion;

	@Column(name = "fecha_solicitud")
	private String fechaSolicitud;

	@Column(name = "fecha_estimada")
	private LocalDateTime fechaEstimada;

	@Column(name = "monto", precision = 10, scale = 2)
	private BigDecimal monto;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull
	private Moneda moneda;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull
	private Estado estado;

	@Column(name = "detalle")
	private String detalle;

	@Column(name = "campo_texto")
	private String campoTexto;

	@Column(name = "campo_numerico")
	private String campoNumerico;

	@Column(name = "tipo")
	private String tipo;
	
	@Column(name = "token")
	private String token;
	
	@JdbcTypeCode(SqlTypes.JSON)
    @Column(name="formato")
    private Map<String, Object> formato;

	@ManyToOne
	@JoinColumn(name = "id_empleado", nullable = false)
	private EmpleadoModel empleado;
	
	@ManyToOne
	@JoinColumn(name = "id_cuenta", nullable = false)
	private CuentaBancariaModel cuenta;
}
