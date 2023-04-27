package com.mentorship.vineservice.services.imp;

import com.mentorship.vineservice.controllers.exeptions.VinePermissionException;
import com.mentorship.vineservice.domain.Order;
import com.mentorship.vineservice.domain.OrderVine;
import com.mentorship.vineservice.domain.PostOffice;
import com.mentorship.vineservice.domain.Vine;
import com.mentorship.vineservice.dto.OrderDto;
import com.mentorship.vineservice.dto.PostOfficeDto;
import com.mentorship.vineservice.dto.VineDto;
import com.mentorship.vineservice.event.model.OrderEvent;
import com.mentorship.vineservice.mapper.VineMapper;
import com.mentorship.vineservice.repositories.OrderRepository;
import com.mentorship.vineservice.repositories.PostOfficeRepository;
import com.mentorship.vineservice.services.OrderService;
import com.mentorship.vineservice.services.VineService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PostOfficeRepository postOfficeRepository;
    private final VineMapper vineMapper;
    private final VineService vineService;
    private final RabbitTemplate rabbitTemplate;

    private static final String CREATE_ORDER_EVENT_MESSAGE = "New order has been created";

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.template.routing-key}")
    private String routingKey;

    @Override
    public Long createOrder(OrderDto orderDto) throws VinePermissionException {

        Order order = vineMapper.orderDtoToOrder(orderDto);
        List<OrderVine> orderVineList = vineMapper.orderVineDtoToOrderVine(orderDto.getVines());

        String homeAddress = orderDto.getDeliveryDetails().getHomeAddress();
        PostOfficeDto postOffice = orderDto.getDeliveryDetails().getPostOffice();

        validateDuplicationDeliveryDetails(homeAddress, postOffice);

        addPostOfficeIfPresent(postOffice, order);

        addVinesToOrder(order, orderVineList);

        Long createdOrderId = orderRepository.save(order).getId();
        log.info(String.format("New order created with id '%s", createdOrderId));

        updateSoldVineAndVineAmount(orderVineList);

        publishCreatedOrderEvent(CREATE_ORDER_EVENT_MESSAGE, createdOrderId);

        return createdOrderId;
    }


    private void validateDuplicationDeliveryDetails(String homeAddress, PostOfficeDto postOffice)
        throws VinePermissionException {
        if (homeAddress == null && postOffice == null) {
            throw new VinePermissionException(HttpStatus.BAD_REQUEST,
                "'homeAddress' and 'postOffice' can not be null simultaneously");
        }

        if (homeAddress != null && postOffice != null) {
            throw new VinePermissionException(HttpStatus.BAD_REQUEST,
                "'homeAddress' and 'postOffice' can not be filled at the same order");
        }
    }

    public void addPostOfficeIfPresent(PostOfficeDto postOffice, Order order) {
        if (postOffice != null) {
            PostOffice postOfficeObject = postOfficeRepository.findPostOfficeByCityAndOfficeNumber(postOffice.getCity(),
                    postOffice.getOfficeNumber())
                .orElseThrow(() -> new NoSuchElementException(
                    String.format("Post office in %s with number %s not exist", postOffice.getCity(),
                        postOffice.getOfficeNumber())));

            order.getDeliveryDetails().setPostOfficeId(postOfficeObject.getId());
            log.info(String.format("Post office id '%s' added to order", postOfficeObject.getId()));
        }
    }

    private void addVinesToOrder(Order order, List<OrderVine> orderVines) {
        order.setVines(new ArrayList<>());
        orderVines.forEach(orders -> {
            Vine vineObject = vineService.getVineById(orders.getOrderVineId().getVineId());
            Integer availableVines = vineObject.getAmount();
            if (orders.getVineAmount() > availableVines) {
                try {
                    throw new VinePermissionException(HttpStatus.BAD_REQUEST,
                        String.format("We can not handle %s bottles of wine %s", orders.getVineAmount(),
                            vineObject.getName()));
                } catch (VinePermissionException e) {
                    throw new RuntimeException(e);
                }
            }
            order.addVine(vineObject, orders.getVineAmount());
            log.info(String.format("Vine '%s' added to order", orders.getOrderVineId().getVineId()));
        });
    }

    private void updateSoldVineAndVineAmount(List<OrderVine> orderVineList) {
        orderVineList.forEach(vine -> {
            Vine vineObject = vineService.getVineById(vine.getOrderVineId().getVineId());
            VineDto vineDto = new VineDto();
            vineDto.setId(vineObject.getId());
            vineDto.setAmount(vineObject.getAmount() - vine.getVineAmount());
            vineDto.setSoldWine(vineObject.getSoldWine() + vine.getVineAmount());
            vineService.updateVineAmount(vineDto);

            log.info(String.format("Vine amount for vineId '%s' updated to  '%s'", vine.getOrderVineId().getVineId(),
                vineDto.getAmount()));
            log.info(String.format("Sold amount for vineId '%s' updated to  '%s'", vine.getOrderVineId().getVineId(),
                vineDto.getSoldWine()));
        });
    }

    private void publishCreatedOrderEvent(String message, Long orderId) {

        OrderEvent orderEvent = new OrderEvent(orderId, message);
        rabbitTemplate.convertAndSend(exchange, routingKey, orderEvent);
    }
}
