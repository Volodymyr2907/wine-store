package com.mentorship.vineservice.service;

import com.mentorship.vineservice.event.model.OrderEvent;
import com.mentorship.vineservice.event.model.OrderEvent.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher {

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.template.routing-key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public void publishOrderEvent(Long orderId, OrderStatus orderStatus) {
        OrderEvent orderEvent = new OrderEvent(orderId, orderStatus);
        rabbitTemplate.convertAndSend(exchange, routingKey, orderEvent);
    }
}
