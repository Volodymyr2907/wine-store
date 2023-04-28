package com.mentorship.vineservice.repository;

import com.mentorship.vineservice.domain.PostOffice;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostOfficeRepository extends JpaRepository<PostOffice, Long> {

    Optional<PostOffice> findPostOfficeByCityAndOfficeNumber(String city, Integer officeNumber);
}
