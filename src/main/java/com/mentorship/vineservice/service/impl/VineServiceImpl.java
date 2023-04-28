package com.mentorship.vineservice.service.impl;

import static com.mentorship.vineservice.specification.VineSpecification.equalsColor;
import static com.mentorship.vineservice.specification.VineSpecification.equalsGrape;
import static com.mentorship.vineservice.specification.VineSpecification.equalsName;
import static com.mentorship.vineservice.specification.VineSpecification.equalsSugar;
import static com.mentorship.vineservice.specification.VineSpecification.equalsYear;

import com.mentorship.vineservice.domain.Vine;
import com.mentorship.vineservice.dto.VineDto;
import com.mentorship.vineservice.dto.VinesDto;
import com.mentorship.vineservice.mapper.VineMapper;
import com.mentorship.vineservice.model.VinesQueryParameters;
import com.mentorship.vineservice.repository.VineRepository;
import com.mentorship.vineservice.service.VineService;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class VineServiceImpl implements VineService {


    private final VineMapper vineMapper;
    private final VineRepository vineRepository;

    public Long saveVine(VineDto vine) {
        return vineRepository.save(vineMapper.vineDtoToVine(vine)).getId();
    }

    public VinesDto getVinesWithFilterAndPagination(VinesQueryParameters vinesQueryParameters) {

        Pageable pageable = PageRequest.of(vinesQueryParameters.getPage(), vinesQueryParameters.getSize());

        Page<Vine> vinePages = vineRepository.findAll(Specification.where(
                equalsName(vinesQueryParameters.getName()))
            .and(equalsSugar(vinesQueryParameters.getSugar()))
            .and(equalsColor(vinesQueryParameters.getColor()))
            .and(equalsGrape(vinesQueryParameters.getGrapeName()))
            .and(equalsYear(vinesQueryParameters.getYear())), pageable);

        return VinesDto.builder()
            .totalCount(vinePages.getTotalElements())
            .vines(vineMapper.vineListToVineDtoList(vinePages.getContent()))
            .build();
    }

    @Override
    public Vine getVineById(Long vineId) {
        return vineRepository.findById(vineId)
            .orElseThrow(() -> new NoSuchElementException(String.format("Vine with id %s not found", vineId)));
    }

    @Override
    public void updateVineAmount(VineDto vineDto) {

        Vine vine = getVineById(vineDto.getId());
        vineMapper.updateVineFromDto(vineDto, vine);
        vineRepository.save(vine);

    }

}
