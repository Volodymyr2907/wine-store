package com.mentorship.vineservice.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderVineIdClass implements Serializable {

    private Long orderId;

    private Long vineId;

}
