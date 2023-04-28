package com.mentorship.vineservice.service;

import com.mentorship.vineservice.event.model.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventService {

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.template.routing-key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;


    public void publishOrderEvent(String message, Long orderId) {

        OrderEvent orderEvent = new OrderEvent(orderId, message);
        rabbitTemplate.convertAndSend(exchange, routingKey, orderEvent);
    }


}
