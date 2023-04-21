package com.mentorship.vineservice.services;

import com.mentorship.vineservice.domain.Order;
import com.mentorship.vineservice.dto.OrderDto;

public interface OrderService {

    Long createOrder(OrderDto orderDto);

    Long createOrder(Order orderDto);

}
