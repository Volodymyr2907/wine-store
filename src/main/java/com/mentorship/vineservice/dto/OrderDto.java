package com.mentorship.vineservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotNull(message = "UserId can not be null")
    @Positive(message = "UserId can not be negative")
    private Long userId;

    private LocalDateTime datetime = LocalDateTime.now();

    @NotNull(message = "Sum can not be null")
    @Positive(message = "Sum can not be negative")
    private Double sum;

    @NotNull(message = "Vines list can not be null")
    private List<OrderVineDto> vines;

    @NotNull(message = "Delivery details can not be null")
    private DeliveryDetailsDto deliveryDetails;


    @Getter
    @Setter
    public static class OrderVineDto {

        private Long vineId;

        private Integer vineAmount;

    }
}
