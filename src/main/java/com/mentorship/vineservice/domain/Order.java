package com.mentorship.vineservice.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order")
@Getter
@Setter
@NoArgsConstructor
public class Order extends BaseEntity {

    @OneToMany(
        mappedBy = "order",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<OrderVine> vines = new ArrayList<>();

    @Column(name = "user_id", length = 36)
    private String userId;

    @Embedded
    private DeliveryDetails deliveryDetails;

    @Column(name = "date_time", nullable = false)
    private LocalDate datetime;

    @Column(name = "sum", nullable = false, precision = 6, scale = 2)
    private double sum;

}
