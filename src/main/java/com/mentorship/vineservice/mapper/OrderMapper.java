package com.mentorship.vineservice.mapper;

import com.mentorship.vineservice.domain.DeliveryDetails;
import com.mentorship.vineservice.domain.Order;
import com.mentorship.vineservice.dto.DeliveryDetailsDto;
import com.mentorship.vineservice.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "vines", ignore = true)
    Order mapOrder(OrderDto orderDto);

    @Mapping(target = "postOffice.id", source = "postOfficeId")
    DeliveryDetails mapDeliveryDetails(DeliveryDetailsDto deliveryDetailsDto);

}
