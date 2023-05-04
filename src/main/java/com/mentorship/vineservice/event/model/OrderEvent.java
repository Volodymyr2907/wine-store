package com.mentorship.vineservice.event.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent implements Serializable {

    private Long orderId;
    private OrderStatus orderStatus;


    public enum OrderStatus {
        CREATED,
        PENDING
    }
}
