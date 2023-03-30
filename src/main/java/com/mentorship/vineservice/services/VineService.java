package com.mentorship.vineservice.services;

import com.mentorship.vineservice.dto.VineDto;
import com.mentorship.vineservice.dto.VinesDto;
import com.mentorship.vineservice.model.VinesQueryParameters;

public interface VineService {

    Long saveVine(VineDto vine);

    VinesDto getVinesWithFilterAndPagination(VinesQueryParameters vinesQueryParameters);
}
