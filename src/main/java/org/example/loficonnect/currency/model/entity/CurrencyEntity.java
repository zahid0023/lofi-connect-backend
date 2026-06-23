package org.example.loficonnect.currency.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.loficonnect.commons.model.entity.AuditableEntity;

@Getter
@Setter
@Entity
@Table(name = "currencies")
public class CurrencyEntity extends AuditableEntity {

    @NotBlank
    @Size(max = 10)
    @Column(name = "code", nullable = false, length = 10)
    private String code;

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 100)
    @Column(name = "description", length = 100)
    private String description;

    @NotBlank
    @Size(max = 10)
    @Column(name = "symbol", nullable = false, length = 10)
    private String symbol;
}
