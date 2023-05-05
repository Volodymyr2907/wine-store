package com.mentorship.vineservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.mentorship.vineservice.domain.DeliveryDetails;
import com.mentorship.vineservice.domain.Order;
import com.mentorship.vineservice.domain.PostOffice;
import com.mentorship.vineservice.domain.Vine;
import com.mentorship.vineservice.dto.DeliveryDetailsDto;
import com.mentorship.vineservice.dto.OrderDto;
import com.mentorship.vineservice.dto.OrderDto.OrderVineDto;
import com.mentorship.vineservice.mapper.OrderMapper;
import com.mentorship.vineservice.repository.OrderRepository;
import com.mentorship.vineservice.service.impl.OrderServiceImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderMapper orderMapper;

    @Mock
    VineService vineService;

    @Mock
    OrderEventPublisher orderEventPublisher;

    @Mock
    PostOfficeService postOfficeService;

    @InjectMocks
    OrderServiceImpl orderService;

    private OrderDto orderDtoWithHomeAddress;
    private Order orderWithHomeAddress;
    private Vine merlotVine;
    private OrderDto orderDtoWithPostOffice;
    private Order orderWithPostOffice;
    private PostOffice postOffice;
    private OrderDto orderDtoWithPostOfficeAndHomeAddress;
    private Vine vineWithInvalidAmount;


    @BeforeEach
    public void setUpOrderTestData() {

        orderDtoWithHomeAddress = createDefaultOrderDto();
        orderWithHomeAddress = mapOrderDtoToOrder(orderDtoWithHomeAddress);

        merlotVine = createVineObject();
        postOffice = createPostOfficeObject();

        orderDtoWithPostOffice = createDefaultOrderDto();
        orderDtoWithPostOffice.getDeliveryDetails().setHomeAddress(null);
        orderDtoWithPostOffice.getDeliveryDetails().setPostOfficeId(1L);

        orderWithPostOffice = mapOrderDtoToOrder(orderDtoWithPostOffice);

        orderDtoWithPostOfficeAndHomeAddress = createDefaultOrderDto();
        orderDtoWithPostOfficeAndHomeAddress.getDeliveryDetails().setPostOfficeId(1L);

        vineWithInvalidAmount = createVineObject();
        vineWithInvalidAmount.setAmount(5);

    }

    @Test
    public void shouldCreateOrderWithHomeAddressAndReturnOrderId() {

        when(orderRepository.save(any(Order.class))).thenReturn(orderWithHomeAddress);

        when(orderMapper.mapOrder(any(OrderDto.class))).thenReturn(orderWithHomeAddress);

        when(vineService.getVineById(any())).thenReturn(merlotVine);

        Long orderId = orderService.createOrder(orderDtoWithHomeAddress);

        assertThat(orderId).isEqualTo(1L);

    }

    @Test
    public void shouldCreateOrderWithPostOfficeAndReturnOrderId() {

        when(orderRepository.save(any(Order.class))).thenReturn(orderWithPostOffice);

        when(orderMapper.mapOrder(any(OrderDto.class))).thenReturn(orderWithPostOffice);

        when(vineService.getVineById(any())).thenReturn(merlotVine);

        when(postOfficeService.findPostOfficeById(any())).thenReturn(postOffice);

        Long orderId = orderService.createOrder(orderDtoWithPostOffice);

        assertThat(orderId).isEqualTo(1L);

    }

    @Test
    public void shouldThrowResponseStatusExceptionWhenCreateOrderWithNotExistingPostOfficeId() {
        Long postOfficeId = 1L;

        when(postOfficeService.findPostOfficeById(postOfficeId)).thenThrow(
            new ResponseStatusException(
                HttpStatus.NOT_FOUND, String.format("Post office with id  %s not exist", postOfficeId)));

        when(orderMapper.mapOrder(any(OrderDto.class))).thenReturn(orderWithPostOffice);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> orderService.createOrder(orderDtoWithPostOffice));

        assertThat(exception.getReason()).isEqualTo(
            String.format("Post office with id  %s not exist", postOfficeId));


    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenCreateOrderWithHomeAddressAndPostOffice() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(orderDtoWithPostOfficeAndHomeAddress);
        });

        assertThat(exception.getMessage()).isEqualTo(
            "'homeAddress' and 'postOfficeId' can not be null or filled simultaneously");
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenRequestedVineAmountIsMoreThenAvailable() {

        when(orderMapper.mapOrder(any(OrderDto.class))).thenReturn(orderWithHomeAddress);

        when(vineService.getVineById(any())).thenReturn(vineWithInvalidAmount);

        OrderVineDto vinesFromOrder = orderDtoWithHomeAddress.getVines().get(0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(orderDtoWithHomeAddress);
        });

        assertThat(exception.getMessage()).isEqualTo(
            String.format("We can not handle %s bottles of wine %s", vinesFromOrder.getVineAmount(),
                vineWithInvalidAmount.getName()));
    }


    private OrderDto createDefaultOrderDto() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setUserId(1L);
        orderDto.setSum(100.30);
        OrderVineDto orderVineDto = new OrderVineDto();
        orderVineDto.setVineId(1L);
        orderVineDto.setVineAmount(10);
        orderDto.setVines(List.of(orderVineDto));
        DeliveryDetailsDto deliveryDetailsDto = new DeliveryDetailsDto();
        deliveryDetailsDto.setUserFirstName("Arctic");
        deliveryDetailsDto.setUserLastName("Monkeys");
        deliveryDetailsDto.setUserEmail("reading@gmail.com");
        deliveryDetailsDto.setPhoneNumber("+39576757575");
        deliveryDetailsDto.setHomeAddress("lukiyanvych str");
        deliveryDetailsDto.setPostOfficeId(null);

        orderDto.setDeliveryDetails(deliveryDetailsDto);

        return orderDto;
    }

    private Order mapOrderDtoToOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setId(orderDto.getId());
        order.setUserId(orderDto.getUserId());
        order.setSum(orderDto.getSum());

        DeliveryDetails deliveryDetails = new DeliveryDetails();
        deliveryDetails.setUserFirstName(orderDto.getDeliveryDetails().getUserFirstName());
        deliveryDetails.setUserLastName(orderDto.getDeliveryDetails().getUserLastName());
        deliveryDetails.setUserEmail(orderDto.getDeliveryDetails().getUserEmail());
        deliveryDetails.setPhoneNumber(orderDto.getDeliveryDetails().getPhoneNumber());
        deliveryDetails.setHomeAddress(orderDto.getDeliveryDetails().getHomeAddress());

        PostOffice postOffice = new PostOffice();
        postOffice.setId(orderDto.getDeliveryDetails().getPostOfficeId());
        deliveryDetails.setPostOffice(postOffice);

        order.setDeliveryDetails(deliveryDetails);

        return order;
    }

    private PostOffice createPostOfficeObject() {
        PostOffice postOffice = new PostOffice();
        postOffice.setId(1L);
        return postOffice;
    }


    private Vine createVineObject() {
        Vine vineMocked = new Vine();
        vineMocked.setId(1L);
        vineMocked.setAmount(100);
        vineMocked.setSoldWine(0);
        vineMocked.setName("MERLOT");

        return vineMocked;
    }

}
