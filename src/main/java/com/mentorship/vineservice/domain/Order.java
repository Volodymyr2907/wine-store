package com.mentorship.vineservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "client_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {

    @OneToMany(
        mappedBy = "order",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<OrderVine> vines = new ArrayList<>();

    @Column(name = "user_id")
    private Long userId;

    @Embedded
    private DeliveryDetails deliveryDetails;

    @JsonIgnore
    @Column(name = "datetime")
    private LocalDateTime datetime = LocalDateTime.now();

    @Column(name = "sum", nullable = false, precision = 6, scale = 2)
    private Double sum;

    public void addVine(Vine vine, Integer vineAmount) {
        OrderVine orderVine = new OrderVine(this, vine, vineAmount);
        vines.add(orderVine);
        vine.getOrders().add(orderVine);
    }

    public void sumOrder() {
        sum = vines.stream().mapToDouble(
            vine -> vine.getVine().getPrice() * vine.getVineAmount()
        ).sum();
    }
}
