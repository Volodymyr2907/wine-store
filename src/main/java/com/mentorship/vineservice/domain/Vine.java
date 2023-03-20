package com.mentorship.vineservice.domain;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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
    private List<OrderVine> orders = new ArrayList<>();

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @Column(name = "grape_name", nullable = false)
    private String grapeName;

    @Column(name = "color", nullable = false, length = 36)
    private String color;

    @Column(name = "is_sparkling", nullable = false)
    private boolean isSparkling;

    @Column(name = "sugar", nullable = false, length = 36)
    private String sugar;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "region")
    private String region;

    @Column(name = "year", nullable = false, length = 4)
    private int year;

    @Column(name = "price", nullable = false, precision = 6, scale = 2)
    private double price;

    @Column(name = "amount", nullable = false, length = 5)
    private int amount;

    @Column(name = "abv", nullable = false, precision = 2, scale = 1)
    private double abv;

}
