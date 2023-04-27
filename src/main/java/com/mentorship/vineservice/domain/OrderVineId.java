package com.mentorship.vineservice.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderVineId implements Serializable {

    @Column(name = "`order_id`")
    private Long orderId;

    @Column(name = "vine_id")
    private Long vineId;
}
