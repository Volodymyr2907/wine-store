package com.mentorship.vineservice.repository;

import com.mentorship.vineservice.domain.Order;
import com.mentorship.vineservice.domain.OrderVine;
import com.mentorship.vineservice.domain.Vine;
import com.mentorship.vineservice.dto.OrderPostOfficeDto;
import java.util.List;
import java.util.Map;

public interface VineRepositoryCustom {

    List<Vine> getAllVines();
    Long getVineIdWithFiltering(Long vineId);
    Long getVineIdFilteredByJoinId();
    List<OrderPostOfficeDto> getAllOrdersIdWithDeliveryInfo();
    List<OrderVine> getVinesWithFilteringInSubQuery();
    List<Order> getAllOrdersWithDeliveryDetails();
    Map<String, Long> getCountedSoldWines();
    List<OrderVine> getVinesWithFilteringInCte();
    void updateVineNameById(String newName, Long id);
    void deletePostOfficeById(Long id);

}
