package com.mentorship.vineservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class DeliveryDetails implements Serializable {


    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "user_first_name", nullable = false)
    private String userFirstName;

    @Column(name = "user_last_name", nullable = false)
    private String userLastName;

    @Column(name = "phone_number", nullable = false, length = 17)
    private String phoneNumber;

    @Column(name = "post_office_id")
    private Long postOfficeId;

    @Column(name = "home_address", length = 1000)
    private String homeAddress = null;

}
