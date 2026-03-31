package org.example.loficonnect.commons.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "limit_keys")
public class LimitKeyEntity extends AuditableEntity {

    @Size(max = 100)
    @NotNull
    @Column(name = "limit_key", nullable = false, length = 100)
    private String limitKey;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Size(max = 50)
    @Column(name = "unit", length = 50)
    private String unit;
}