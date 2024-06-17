package com.mentorship.vineservice.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class OrderPostOfficeDto {

    private Long orderId;
    private String city;
    private Integer office_number;

}
