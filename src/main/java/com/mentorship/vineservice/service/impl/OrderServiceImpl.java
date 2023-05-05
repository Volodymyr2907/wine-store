package com.mentorship.vineservice.service.impl;

import com.mentorship.vineservice.domain.Order;
import com.mentorship.vineservice.domain.PostOffice;
import com.mentorship.vineservice.domain.Vine;
import com.mentorship.vineservice.dto.DeliveryDetailsDto;
import com.mentorship.vineservice.dto.OrderDto;
import com.mentorship.vineservice.dto.OrderDto.OrderVineDto;
import com.mentorship.vineservice.event.model.OrderEvent.OrderStatus;
import com.mentorship.vineservice.mapper.OrderMapper;
import com.mentorship.vineservice.repository.OrderRepository;
import com.mentorship.vineservice.service.OrderEventPublisher;
import com.mentorship.vineservice.service.OrderService;
import com.mentorship.vineservice.service.PostOfficeService;
import com.mentorship.vineservice.service.VineService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final VineService vineService;
    private final OrderEventPublisher orderEventPublisher;
    private final PostOfficeService postOfficeService;

    public Long createOrder(OrderDto orderDto) {
        validateIfDeliveryDetailsAreNotDuplicated(orderDto.getDeliveryDetails());

        Order order = orderMapper.mapOrder(orderDto);
        processDeliveryByPostInformation(orderDto.getDeliveryDetails(), order);
        addVinesToOrder(order, orderDto.getVines());

        Long createdOrderId = orderRepository.save(order).getId();
        log.info(String.format("New order created with id '%s", createdOrderId));

        orderEventPublisher.publishOrderEvent(createdOrderId, OrderStatus.CREATED);

        return createdOrderId;
    }


    private void validateIfDeliveryDetailsAreNotDuplicated(DeliveryDetailsDto deliveryDetailsDto) {

        boolean homeAddressAndPostOfficeAreNull =
            deliveryDetailsDto.getHomeAddress() == null && deliveryDetailsDto.getPostOfficeId() == null;
        boolean homeAddressAndPostOfficeAreNotNull =
            deliveryDetailsDto.getHomeAddress() != null && deliveryDetailsDto.getPostOfficeId() != null;

        if (homeAddressAndPostOfficeAreNull || homeAddressAndPostOfficeAreNotNull) {
            throw new IllegalArgumentException(
                "'homeAddress' and 'postOfficeId' can not be null or filled simultaneously");
        }
    }

    private void processDeliveryByPostInformation(DeliveryDetailsDto deliveryDetails, Order order) {
        Long postOfficeId = deliveryDetails.getPostOfficeId();
        if (postOfficeId != null) {
            PostOffice postOffice = postOfficeService.findPostOfficeById(postOfficeId);

            order.getDeliveryDetails().setPostOffice(postOffice);
            log.info(String.format("Post office id '%s' added to order", postOffice.getId()));
        }
    }

    private void addVinesToOrder(Order order, List<OrderVineDto> vineListInOrder) {

        vineListInOrder.forEach(vineWithAmount -> {
            Vine vine = vineService.getVineById(vineWithAmount.getVineId());

            Integer availableVineAmount = vine.getAmount();
            Integer vineAmountFromOrder = vineWithAmount.getVineAmount();

            if (vineAmountFromOrder <= availableVineAmount) {
                order.addVine(vine, vineAmountFromOrder);
                vine.sellVines(vineAmountFromOrder);

                log.info(String.format("Vine '%s' added to order", vineWithAmount.getVineId()));
            } else {
                throw new IllegalArgumentException(
                    String.format("We can not handle %s bottles of wine %s", vineAmountFromOrder,
                        vine.getName()));
            }
        });
    }
}
