package com.mentorship.vineservice.dto;

import java.time.LocalDateTime;
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

    private List<OrderVineDto> vines;

    private DeliveryDetailsDto deliveryDetails;


    @Getter
    @Setter
    public static class OrderVineDto {

        private Long vineId;

        private Integer vineAmount;

    }
}
