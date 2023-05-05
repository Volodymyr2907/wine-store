package com.mentorship.vineservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentorship.vineservice.controller.exeption.ErrorResponse;
import com.mentorship.vineservice.dto.DeliveryDetailsDto;
import com.mentorship.vineservice.dto.OrderDto;
import com.mentorship.vineservice.dto.OrderDto.OrderVineDto;
import com.mentorship.vineservice.dto.enums.UserRole;
import com.mentorship.vineservice.service.OrderService;
import com.mentorship.vineservice.service.PermissionValidator;
import com.mentorship.vineservice.service.VineService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = VineController.class)
public class OrderControllerTest {

    private static final String CREATE_ORDER_URL = "/api/vine-service/order";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String RANDOM_TOKEN = "random token";

    @MockBean
    PermissionValidator permissionValidationService;

    @MockBean
    OrderService orderService;

    @MockBean
    VineService vineService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    private OrderDto validOrder;
    private OrderDto orderWithMissingUserId;
    private OrderDto orderWithNegativeSum;

    @BeforeEach
    public void setUpOrderTestData() {

        validOrder = createOrderDto(1L, 100.4);
        orderWithMissingUserId = createOrderDto(null, 100.4);
        orderWithNegativeSum = createOrderDto(1L, -100.4);

    }


    @Test
    public void shouldCreateOrderAndReturnOrderIdWithStatusCreated() throws Exception {

        doNothing().when(permissionValidationService).validateUserPermission(isA(String.class), isA(UserRole.class));

        when(orderService.createOrder(any(OrderDto.class))).thenReturn(1L);

        mockMvc.perform(post(CREATE_ORDER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, RANDOM_TOKEN)
                .content(objectMapper.writeValueAsString(validOrder)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().string("1"));

    }

    @Test
    public void shouldNotCreateOrderIfUserIdMissingAndReturnBadRequest() throws Exception {

        doNothing().when(permissionValidationService).validateUserPermission(isA(String.class), isA(UserRole.class));

        String responseContent = mockMvc.perform(post(CREATE_ORDER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, RANDOM_TOKEN)
                .content(objectMapper.writeValueAsString(orderWithMissingUserId)))
            .andExpect(status().isBadRequest())
            .andReturn().getResponse().getContentAsString();

        assertThat(getErrorListFromResponse(responseContent)).containsOnly("UserId can not be null");
    }

    @Test
    public void shouldNotCreateOrderIfSumValueIsNegativeAndReturnBadRequest() throws Exception {

        doNothing().when(permissionValidationService).validateUserPermission(isA(String.class), isA(UserRole.class));

        String responseContent = mockMvc.perform(post(CREATE_ORDER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, RANDOM_TOKEN)
                .content(objectMapper.writeValueAsString(orderWithNegativeSum)))
            .andExpect(status().isBadRequest())
            .andReturn().getResponse().getContentAsString();

        assertThat(getErrorListFromResponse(responseContent)).containsOnly("Sum can not be negative");
    }


    private OrderDto createOrderDto(Long userId, Double sum) {
        DeliveryDetailsDto deliveryDetailsDto = new DeliveryDetailsDto();
        deliveryDetailsDto.setHomeAddress("HomeAddress");

        OrderDto.OrderVineDto orderVineDto = new OrderVineDto();
        orderVineDto.setVineId(1L);
        orderVineDto.setVineAmount(10);

        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(userId);
        orderDto.setSum(sum);
        orderDto.setVines(List.of(orderVineDto));
        orderDto.setDeliveryDetails(deliveryDetailsDto);
        return orderDto;
    }

    private List<String> getErrorListFromResponse(String responseAsString) throws JsonProcessingException {
        return objectMapper.readValue(responseAsString, ErrorResponse.class).getErrors();
    }
}
