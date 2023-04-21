package com.mentorship.vineservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "`order`")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {

//    @OneToMany(
//       // mappedBy = "order",
//        fetch = FetchType.LAZY,
//        cascade = CascadeType.ALL,
//        orphanRemoval = true
//    )
//    @JoinColumn(name = "`order_id`")
//    @ElementCollection(targetClass = OrderVineId.class, fetch = FetchType.LAZY)
//    @CollectionTable(name = "order_vine",
//        joinColumns = @JoinColumn(name = "`order_id`"))
//
//    @OneToMany(
//        cascade = CascadeType.ALL,
//        orphanRemoval = true
//    )
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "`order_id`", referencedColumnName = "id")
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

}
