package org.example.loficonnect.auth.model.enitty;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.loficonnect.commons.model.entity.AuditableEntity;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class RoleEntity extends AuditableEntity {
    @Column(name = "name", nullable = false, length = 50)
    private String name;
}