package com.mentorship.vineservice.service;

import com.mentorship.vineservice.domain.Vine;
import com.mentorship.vineservice.dto.VineDto;
import com.mentorship.vineservice.dto.VinesDto;
import com.mentorship.vineservice.model.VinesQueryParameters;

public interface VineService {

    Long saveVine(VineDto vine);

    VinesDto getVinesWithFilterAndPagination(VinesQueryParameters vinesQueryParameters);

    Vine getVineById(Long vineId);

    void updateVineAmount(VineDto vineDto);
}
