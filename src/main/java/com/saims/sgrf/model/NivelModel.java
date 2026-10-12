package com.saims.sgrf.model;

import com.saims.sgrf.base.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "nivel")
public class NivelModel extends BaseEntity{
	
	@ManyToOne
    @JoinColumn(name = "id_padre", nullable = false)
    private AreaModel padre;
	
	@ManyToOne
    @JoinColumn(name = "id_hijo", nullable = false)
    private AreaModel hijo;
}
