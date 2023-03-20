package com.mentorship.vineservice.services.imp;

import com.mentorship.vineservice.domain.Vine;
import com.mentorship.vineservice.dto.PaginationDto;
import com.mentorship.vineservice.repositories.VineRepository;
import com.mentorship.vineservice.services.VineService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class VineServiceImpl implements VineService {


    private final VineRepository vineRepository;

    public VineServiceImpl(VineRepository vineRepository) {
        this.vineRepository = vineRepository;
    }

    public Long saveVineAndReturnId(Vine vine) {
        return vineRepository.save(vine).getId();
    }

    public Page<Vine> getVinesWithFilterAndPagination(PaginationDto pagination, String sugar, String color, String name,
        String grape) {
        return vineRepository.findVinesBySugarAndColorAndTypeAndGrapePaginated(pagination, sugar, color, name, grape);
    }

}
