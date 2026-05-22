package org.example.loficonnect.commons.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "countries")
public class CountryEntity extends AuditableEntity {

    @Size(max = 10)
    @NotNull
    @Column(name = "code", nullable = false, length = 10)
    private String code;

    @Size(max = 10)
    @Column(name = "iso3_code", length = 10)
    private String iso3Code;

    @Size(max = 10)
    @Column(name = "phone_code", length = 10)
    private String phoneCode;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;
}