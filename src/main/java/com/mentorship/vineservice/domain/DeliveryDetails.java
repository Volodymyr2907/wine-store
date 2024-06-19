package com.mentorship.vineservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;


@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDetails implements Serializable {


    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "user_first_name", nullable = false)
    private String userFirstName;

    @Column(name = "user_last_name", nullable = false)
    private String userLastName;

    @Column(name = "phone_number", nullable = false, length = 17)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_office_id")
    private PostOffice postOffice;

    @Column(name = "home_address", length = 1000)
    private String homeAddress;

}
