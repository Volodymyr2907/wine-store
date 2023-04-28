package com.mentorship.vineservice.repository;

import com.mentorship.vineservice.domain.Vine;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VineRepository extends JpaRepository<Vine, Long>, JpaSpecificationExecutor<Vine> {

    Optional<Vine> findById(Long vineId);
}
