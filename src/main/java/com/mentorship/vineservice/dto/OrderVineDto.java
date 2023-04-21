package com.mentorship.vineservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderVineDto {

    private Long orderId;

    private Long vineId;

    private Integer vineAmount;

}
