package com.mentorship.vineservice.repositories;

import com.mentorship.vineservice.domain.Vine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VineRepository extends JpaRepository<Vine, Long>, VineRepositoryCustom {

}
