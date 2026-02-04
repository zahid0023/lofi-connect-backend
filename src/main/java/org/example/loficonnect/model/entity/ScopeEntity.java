package org.example.loficonnect.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.loficonnect.commons.model.entity.AuditableEntity;

@Getter
@Setter
@Entity
@Table(name = "scopes")
public class ScopeEntity extends AuditableEntity {
    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;
}