package com.mentorship.vineservice.repositories;

import com.mentorship.vineservice.domain.PostOffice;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostOfficeRepository extends JpaRepository<PostOffice, Long>, JpaSpecificationExecutor<PostOffice> {

    Optional<PostOffice> findPostOfficeByCityAndOfficeNumber(String city, Integer officeNumber);
}
