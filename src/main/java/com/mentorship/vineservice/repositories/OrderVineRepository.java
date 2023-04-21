package com.mentorship.vineservice.repositories;

import com.mentorship.vineservice.domain.Order;
import com.mentorship.vineservice.domain.OrderVine;
import com.mentorship.vineservice.domain.OrderVineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderVineRepository extends JpaRepository<OrderVine, OrderVineId>,
    JpaSpecificationExecutor<OrderVine> {

}
