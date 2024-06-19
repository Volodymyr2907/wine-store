package com.mentorship.vineservice.repository;

import com.mentorship.vineservice.domain.Order;
import com.mentorship.vineservice.domain.OrderVine;
import com.mentorship.vineservice.domain.Vine;
import com.mentorship.vineservice.dto.OrderPostOfficeDto;
import java.util.List;
import java.util.Map;

public interface VineRepositoryCustom {

    List<Vine> getAllVines();

    List<Long> getVineIdsWithFilterByName(String name);

    String getVineNameWithFilterByJoinedOrderId(Long orderId);

    List<OrderPostOfficeDto> getOrderPostOfficeDto();

    List<OrderVine> getOrderVinesWithFilteringInSubQuery(Integer from, Integer to);

    List<Order> getAllOrdersWithFetchedDeliveryDetails();

    Map<String, Long> getMapVineNameToSoldVine();

    List<OrderVine> getOrderVinesWithFilteringInCte(Integer from, Integer to);

    void updateVineNameById(String newName, Long id);

    void deletePostOfficeById(Long id);

}
