package com.mentorship.vineservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "order_vine")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderVine {

    @EmbeddedId
    private OrderVineId orderVineId;

    @ManyToOne
    @PrimaryKeyJoinColumn
    @MapsId("orderId")
    private Order order;

    @ManyToOne
    @PrimaryKeyJoinColumn
    @MapsId("vineId")
    private Vine vine;

    @Column(name = "vine_amount", nullable = false, length = 5)
    private Integer vineAmount;
}

//@Entity
//@Table(name = "order_vine")
//@Getter
//@Setter
//@NoArgsConstructor
//@IdClass(OrderVineIdClass.class)
//public class OrderVine implements Serializable {
//
//    @Id
//    @Column(name = "`order_id`")
//    @JsonIgnore
//    private Long orderId;
//
//    @Id
//    @Column(name = "vine_id")
//    private Long vineId;
//
////    @ManyToOne(fetch = FetchType.LAZY)
////    @MapsId("orderId")
////    private Order order;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("vineId")
//    private Vine vine;
//
//    @Column(name = "vine_amount", nullable = false, length = 5)
//    private Integer vineAmount;
//}