package com.mentorship.vineservice.mapper;

import com.mentorship.vineservice.domain.DeliveryDetails;
import com.mentorship.vineservice.domain.Order;
import com.mentorship.vineservice.domain.OrderVine;
import com.mentorship.vineservice.domain.Vine;
import com.mentorship.vineservice.dto.DeliveryDetailsDto;
import com.mentorship.vineservice.dto.OrderDto;
import com.mentorship.vineservice.dto.OrderVineDto;
import com.mentorship.vineservice.dto.VineDto;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface VineMapper {

    Vine vineDtoToVine(VineDto vineDto);

    Order orderDtoToOrder(OrderDto orderDto);

    List<OrderVine> orderVineDtoToOrderVine(List<OrderVineDto> orderVineDto);

    @Mapping(target = "orderVineId.orderId", source = "orderId")
    @Mapping(target = "orderVineId.vineId", source = "vineId")
    OrderVine dtoToOrderVine(OrderVineDto orderVineDto);

    DeliveryDetails dtoToDeliveryDetails(DeliveryDetailsDto deliveryDetailsDto);

    List<VineDto> vineListToVineDtoList(List<Vine> vines);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateVineFromDto(VineDto dto, @MappingTarget Vine entity);

}
