package com.mentorship.vineservice.repositories;

import com.mentorship.vineservice.domain.Vine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VineRepository extends JpaRepository<Vine, Long>, JpaSpecificationExecutor<Vine> {

}
