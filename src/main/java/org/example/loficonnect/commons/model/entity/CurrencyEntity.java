package org.example.loficonnect.commons.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "currencies")
public class CurrencyEntity extends AuditableEntity {

    @Size(max = 10)
    @NotNull
    @Column(name = "code", nullable = false, length = 10)
    private String code;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 100)
    @NotNull
    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @Size(max = 10)
    @NotNull
    @Column(name = "symbol", nullable = false, length = 10)
    private String symbol;


}