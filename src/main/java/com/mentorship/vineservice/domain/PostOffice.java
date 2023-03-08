package com.mentorship.vineservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "post_office")
@Getter
@Setter
@NoArgsConstructor
public class PostOffice extends BaseEntity {

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "office_number", nullable = false, length = 5)
    private int officeNumber;

    @Column(name = "office_address", nullable = false)
    private String officeAddress;

}
