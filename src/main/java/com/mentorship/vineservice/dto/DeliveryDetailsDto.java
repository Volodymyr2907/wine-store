package com.mentorship.vineservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDetailsDto {

    private String userEmail;

    private String userFirstName;

    private String userLastName;

    private String phoneNumber;

    private PostOfficeDto postOffice;

    private String homeAddress;

}
