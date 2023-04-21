package com.mentorship.vineservice.services.imp;

import com.mentorship.vineservice.domain.DeliveryDetails;
import com.mentorship.vineservice.domain.Order;
import com.mentorship.vineservice.domain.OrderVine;
import com.mentorship.vineservice.dto.OrderDto;
import com.mentorship.vineservice.dto.OrderVineDto;
import com.mentorship.vineservice.mapper.VineMapper;
import com.mentorship.vineservice.repositories.OrderRepository;
import com.mentorship.vineservice.repositories.OrderVineRepository;
import com.mentorship.vineservice.services.OrderService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderVineRepository orderVineRepository;
    private final VineMapper vineMapper;
    private Order dd;


    @Override
    public Long createOrder(OrderDto orderDto) {

//        Order order = vineMapper.orderDtoToOrder(orderDto);
//
//        order.setUserId(orderDto.getUserId());
//        order.setDatetime(orderDto.getDatetime());
//        order.setSum(orderDto.getSum());
//
//        order.setVines(vineMapper.orderVineDtoToOrderVine(orderDto.getVines()));
//        order.setDeliveryDetails(vineMapper.dtoToDeliveryDetails(orderDto.getDeliveryDetails()));

        return null; //orderRepository.save(order).getId();
    }

    @Override
    public Long createOrder(Order order) {


        Order newOrder = new Order();

        newOrder.setUserId(order.getUserId());
        newOrder.setDatetime(order.getDatetime());
        newOrder.setSum(order.getSum());

        List<OrderVine> orderVineList = new ArrayList<>();

        order.getVines().forEach(
            vine -> {
                OrderVine orderVine = new OrderVine();
                orderVine.setVineAmount(vine.getVineAmount());
                orderVine.setVine(vine.getVine());

                //orderVine.setVine(vine.getVine());
                //orderVineRepository.save(orderVine);
                orderVineList.add(orderVine);
            }
        );

        newOrder.setVines(orderVineList);

//        DeliveryDetails deliveryDetails = new DeliveryDetails();
//        deliveryDetails.setHomeAddress(order.getDeliveryDetails().getHomeAddress());
//        deliveryDetails.setPhoneNumber(order.getDeliveryDetails().getPhoneNumber());
//        deliveryDetails.setPostOfficeId(order.getDeliveryDetails().getPostOfficeId());
//        deliveryDetails.setUserFirstName(order.getDeliveryDetails().getUserFirstName());
//        deliveryDetails.setUserLastName(order.getDeliveryDetails().getUserLastName());
//        deliveryDetails.setUserEmail(order.getDeliveryDetails().getUserEmail());


        newOrder.setDeliveryDetails(order.getDeliveryDetails());

        return orderRepository.save(newOrder).getId();
    }
}
