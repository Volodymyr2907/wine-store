package com.mentorship.vineservice.service.impl;

import com.mentorship.vineservice.controller.exeption.VinePermissionException;
import com.mentorship.vineservice.domain.Order;
import com.mentorship.vineservice.domain.OrderVine;
import com.mentorship.vineservice.domain.PostOffice;
import com.mentorship.vineservice.domain.Vine;
import com.mentorship.vineservice.dto.OrderDto;
import com.mentorship.vineservice.dto.PostOfficeDto;
import com.mentorship.vineservice.dto.VineDto;
import com.mentorship.vineservice.mapper.VineMapper;
import com.mentorship.vineservice.repository.OrderRepository;
import com.mentorship.vineservice.repository.PostOfficeRepository;
import com.mentorship.vineservice.service.EventService;
import com.mentorship.vineservice.service.OrderService;
import com.mentorship.vineservice.service.PostOfficeService;
import com.mentorship.vineservice.service.VineService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final VineMapper vineMapper;
    private final VineService vineService;
    private final EventService eventService;
    private final PostOfficeService postOfficeService;

    @Override
    public Long createOrder(OrderDto orderDto) throws VinePermissionException {

        String homeAddress = orderDto.getDeliveryDetails().getHomeAddress();
        PostOfficeDto postOffice = orderDto.getDeliveryDetails().getPostOffice();

        validateDuplicatedDeliveryDetails(homeAddress, postOffice);

        Order order = vineMapper.orderDtoToOrder(orderDto);
        List<OrderVine> orderVineList = vineMapper.orderVineDtoToOrderVine(orderDto.getVines());

        addPostOfficeIfPresent(postOffice, order);

        addVinesToOrder(order, orderVineList);

        Long createdOrderId = orderRepository.save(order).getId();
        log.info(String.format("New order created with id '%s", createdOrderId));

        updateSoldVineAndVineAmount(orderVineList);

        eventService.publishOrderEvent("New order has been created", createdOrderId);

        return createdOrderId;
    }


    private void validateDuplicatedDeliveryDetails(String homeAddress, PostOfficeDto postOffice)
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

    private void addPostOfficeIfPresent(PostOfficeDto postOffice, Order order) {
        if (postOffice != null) {
            PostOffice postOfficeObject = postOfficeService.findPostOfficeByCityAndOfficeNumber(postOffice.getCity(),
                postOffice.getOfficeNumber());

            order.getDeliveryDetails().setPostOfficeId(postOfficeObject.getId());
            log.info(String.format("Post office id '%s' added to order", postOfficeObject.getId()));
        }
    }

    private void addVinesToOrder(Order order, List<OrderVine> orderVines) {
        order.setVines(new ArrayList<>());
        orderVines.forEach(orderVine -> {
            Vine vineObject = vineService.getVineById(orderVine.getOrderVineId().getVineId());
            Integer availableVines = vineObject.getAmount();
            if (orderVine.getVineAmount() > availableVines) {
                try {
                    throw new IllegalArgumentException(
                        String.format("We can not handle %s bottles of wine %s", orderVine.getVineAmount(),
                            vineObject.getName()));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
                }
            }
            {
                order.addVine(vineObject, orderVine.getVineAmount());
                log.info(String.format("Vine '%s' added to order", orderVine.getOrderVineId().getVineId()));
            }
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
}
