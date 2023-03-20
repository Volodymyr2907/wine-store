package com.mentorship.vineservice.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;


@Entity
@Table(name = "vine")
@Getter
@Setter
@NoArgsConstructor
public class Vine extends BaseEntity {

    @OneToMany(
        mappedBy = "vine",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonIgnore
    private List<OrderVine> orders = new ArrayList<>();

    @NotBlank(message = "Name can not be empty or null")
    @Length(max = 255, message = "Name name is too long")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Manufacturer can not be empty or null")
    @Length(max = 255, message = "Manufacturer name is too long")
    @Column(name = "manufacturer")
    private String manufacturer;

    @NotBlank(message = "Grape name can not be empty or null")
    @Length(max = 255, message = "Grape name name is too long")
    @Column(name = "grape_name")
    private String grapeName;

    @NotBlank(message = "Color can not be empty or null")
    @Length(max = 36, message = "Color name is too long")
    @Column(name = "color")
    private String color;

    @Column(name = "is_sparkling")
    private Boolean isSparkling;

    @NotBlank(message = "Sugar can not be empty or null")
    @Length(max = 36, message = "Sugar name is too long")
    @Column(name = "sugar")
    private String sugar;

    @NotBlank(message = "Country can not be empty or null")
    @Length(max = 255, message = "Country name is too long")
    @Column(name = "country")
    private String country;

    @Length(max = 255, message = "Region name is too long")
    @Column(name = "region")
    private String region;

    @Positive(message = "Year must be greater then 0")
    @Column(name = "year", nullable = false, length = 4)
    private Integer year;

    @Range(min = 0, max = 999999, message = "Price should be from 0 to 999999")
    @Column(name = "price", nullable = false, precision = 6, scale = 2)
    private Double price;

    @Positive(message = "Amount must be greater then 0")
    @Column(name = "amount", nullable = false, length = 5)
    private Integer amount;

    @Range(min = 0, max = 30, message = "Abv should be from 0 to 30")
    @Column(name = "abv", nullable = false)
    private Double abv;

}
