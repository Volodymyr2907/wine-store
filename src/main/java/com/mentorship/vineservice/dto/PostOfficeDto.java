package com.mentorship.vineservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostOfficeDto {

    private Long id;

    private String city;

    private Integer officeNumber;

    private String officeAddress;

}
