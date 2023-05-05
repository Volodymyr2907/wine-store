package com.mentorship.vineservice.domain;


import com.mentorship.vineservice.dto.enums.VineColor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "vine")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vine extends BaseEntity {

    @OneToMany(
        mappedBy = "vine",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<OrderVine> orders = new ArrayList<>();

    @Column(name = "name")
    private String name;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "grape_name")
    private String grapeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "color")
    private VineColor color;

    @Column(name = "is_sparkling")
    private Boolean isSparkling;

    @Column(name = "sugar")
    private String sugar;

    @Column(name = "country")
    private String country;

    @Column(name = "region")
    private String region;

    @Column(name = "'year'", nullable = false, length = 4)
    private Integer year;

    @Column(name = "price", nullable = false, precision = 6, scale = 2)
    private Double price;

    @Column(name = "amount", nullable = false, length = 5)
    private Integer amount;

    @Column(name = "abv", nullable = false)
    private Double abv;

    @Column(name = "sold_wine")
    private Integer soldWine;

    public void sellVines(Integer amount) {
        this.amount -= amount;
        this.soldWine += amount;
    }
}
