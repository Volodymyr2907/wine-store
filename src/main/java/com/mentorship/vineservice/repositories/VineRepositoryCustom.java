package com.mentorship.vineservice.repositories;

import com.mentorship.vineservice.domain.Vine;
import com.mentorship.vineservice.dto.PaginationDto;
import org.springframework.data.domain.Page;

public interface VineRepositoryCustom {
    Page<Vine> findVinesBySugarAndColorAndTypeAndGrapePaginated(PaginationDto pagination, String sugar, String color,
        String name, String grape);


}
