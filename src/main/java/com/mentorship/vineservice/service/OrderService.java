package com.mentorship.vineservice.service;

import com.mentorship.vineservice.controller.exeption.VinePermissionException;
import com.mentorship.vineservice.dto.OrderDto;

public interface OrderService {

    Long createOrder(OrderDto orderDto) throws VinePermissionException;
}
