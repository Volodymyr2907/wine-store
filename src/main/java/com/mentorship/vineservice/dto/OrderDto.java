package com.mentorship.vineservice.dto;

import com.mentorship.vineservice.domain.OrderVine;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {


    private Long id;

    private Long userId;

    private LocalDateTime datetime = LocalDateTime.now();

    private Double sum;

    private List<OrderVineDto> vines = new ArrayList<>();

    private DeliveryDetailsDto deliveryDetails;

}
