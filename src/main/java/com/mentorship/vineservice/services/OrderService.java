package com.mentorship.vineservice.services;

import com.mentorship.vineservice.controllers.exeptions.VinePermissionException;
import com.mentorship.vineservice.dto.OrderDto;

public interface OrderService {

    Long createOrder(OrderDto orderDto) throws VinePermissionException;
}
