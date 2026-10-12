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
@Table(name = "rdg")
public class RdgModel extends BaseEntity{
	
	@Column(name = "numero")
	private String numero;
	
	@Column(name = "gestion")
	private int gestion;
	
	@Column(name = "fecha_rendicion")
	private String fechaRendicion;
	
	@Column(name = "fecha_solicitud")
	private String fechaSolicitud;
	
	@Column(name = "importe", precision = 10, scale = 2)
	private BigDecimal importe;
	
	@Column(name = "detalle")
	private String detalle;
	
	@Column(name = "actividad")
	private String actividad;
	
	@Column(name = "del")
	private LocalDateTime del;
	
	@Column(name = "al")
	private LocalDateTime al;
	
	@Column(name = "participantes")
	private String participantes;
	
	@Column(name = "gasto", precision = 10, scale = 2)
	private BigDecimal gasto;
	
	@Column(name = "saldo", precision = 10, scale = 2)
	private BigDecimal saldo;
	
	@Column(name = "rembolso", precision = 10, scale = 2)
	private BigDecimal rembolso;
	
	@Column(name = "devolver", precision = 10, scale = 2)
	private BigDecimal devolver;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull
	private Moneda moneda;
	
	@Column(name = "tc", precision = 10, scale = 2)
	private BigDecimal tipoCambio;
	
	@Column(name = "cargo")
	private String cargo;
	
	@Column(name = "area")
	private String area;
	
	@Column(name = "cuenta")
	private String cuenta;
	
	@Column(name = "tipo_cuenta")
	private String tipoCuenta;
	
	@Column(name = "banco")
	private String banco;
	
	@Column(name = "centro_costos")
	private String centroCostos;
	
	@Column(name = "codigo")
	private String codigo;
	
	@Column(name = "token")
	private String token;
	
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull
	private Estado estado;
	
	/**
     * 👤 Relación con Solicitudes 
     * una randicion -> una solicitud
     */
	@Column(name = "id_srf")
	private Long idSrf;	
	
	/**
     * 👤 Relación con Empleado
     * muchos cargos -> empleado
     */
    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    private EmpleadoModel empleado;
    
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name="formato")
    private Map<String, Object> formato;

}
