package com.saims.sgrf.model;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.saims.sgrf.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "data")
public class SettingsModel extends BaseEntity{
	
	@Column(name = "nombre")
    private String nombre;
	
	@Column(name = "categoria")
    private String categoria;
	
	@JdbcTypeCode(SqlTypes.JSON)
    @Column(name="value",columnDefinition = "json")
    private Map<String, Object> value;

}
