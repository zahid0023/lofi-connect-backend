package org.example.loficonnect.subscription.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.loficonnect.commons.model.entity.AuditableEntity;
import org.example.loficonnect.subscription.model.enums.LimitKeyCategory;
import org.example.loficonnect.subscription.model.enums.LimitKeyDataType;
import org.example.loficonnect.subscription.model.enums.LimitKeyUnit;

@Getter
@Setter
@Entity
@Table(name = "limit_keys")
public class LimitKeyEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "code", nullable = false, length = 100, unique = true)
    private String code;

    @NotBlank
    @Size(max = 150)
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false, length = 50)
    private LimitKeyDataType dataType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    private LimitKeyCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit", length = 50)
    private LimitKeyUnit unit;
}
