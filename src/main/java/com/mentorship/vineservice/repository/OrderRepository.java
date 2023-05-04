package com.mentorship.vineservice.repository;

import com.mentorship.vineservice.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
