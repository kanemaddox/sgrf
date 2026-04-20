package com.saims.sgrf.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class StatusEntity extends BaseEntity {
	

    @Column(nullable = false)
    protected Boolean estado = true;
}
