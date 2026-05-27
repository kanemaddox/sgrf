package com.saims.sgrf.model;

import java.time.LocalDateTime;
import java.util.List;

import com.saims.sgrf.base.BaseEntity;
import com.saims.sgrf.enums.Estado;
import com.saims.sgrf.enums.Moneda;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

	@Column(name = "monto")
	private double monto;

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
	private String campo_texto;

	@Column(name = "campo_numerico")
	private String campo_numerico;

	@Column(name = "tipo")
	private String tipo;

	@ManyToOne
	@JoinColumn(name = "id_empleado", nullable = false)
	private EmpleadoModel empleado;
	
	@ManyToOne
	@JoinColumn(name = "id_cuenta", nullable = false)
	private CuentaBancariaModel cuenta;
}
