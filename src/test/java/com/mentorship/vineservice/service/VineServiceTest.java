package com.mentorship.vineservice.service;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.mentorship.vineservice.domain.Vine;
import com.mentorship.vineservice.dto.VineDto;
import com.mentorship.vineservice.dto.enums.VineColor;
import com.mentorship.vineservice.mapper.VineMapper;
import com.mentorship.vineservice.repository.VineRepository;
import com.mentorship.vineservice.service.impl.VineServiceImpl;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
public class VineServiceTest {

    @Mock
    VineMapper vineMapper;

    @Mock
    VineRepository vineRepository;

    @InjectMocks
    VineServiceImpl vineService;


    private VineDto merlotVineDto;
    private Vine merlotVine;

    @BeforeEach
    public void detUpVineTestData() {

        merlotVineDto = createVineDto();
        merlotVine = mapVineDtoToVine(merlotVineDto);

    }

    @Test
    public void shouldSaveVineAndReturnId() {
        when(vineRepository.save(any())).thenReturn(merlotVine);

        Long createdVine = vineService.saveVine(merlotVineDto);

        assertThat(createdVine).isEqualTo(merlotVine.getId());

    }

    @Test
    public void shouldReturnVineById() {

        Long vineId = 1L;

        when(vineRepository.findById(vineId)).thenReturn(Optional.ofNullable(merlotVine));

        Vine actualVine = vineService.getVineById(vineId);

        assertThat(actualVine).isEqualTo(merlotVine);

    }

    @Test
    public void shouldReturnResponseStatusExceptionIfVineNotExist() {

        Long vineId = 1L;

        when(vineRepository.findById(vineId)).thenThrow(new ResponseStatusException(
            HttpStatus.NOT_FOUND, String.format("Vine with id %s not found", vineId)));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            vineService.getVineById(vineId);
        });

        Assertions.assertThat(exception.getReason()).isEqualTo(
            String.format("Vine with id %s not found", vineId));
    }


    private VineDto createVineDto() {
        VineDto vineDto = new VineDto();
        vineDto.setId(1L);
        vineDto.setName("MERLOT VINE");
        vineDto.setColor(VineColor.RED);
        vineDto.setCountry("UKRAINE");
        vineDto.setManufacturer("SHABO");
        vineDto.setYear(2019);
        vineDto.setSugar("DRY");
        vineDto.setGrapeName("MERLOT");
        vineDto.setAmount(100);
        vineDto.setSoldWine(10);
        vineDto.setAbv(12.2);
        vineDto.setIsSparkling(false);
        vineDto.setPrice(200.00);
        vineDto.setRegion("KHERSON");

        return vineDto;
    }

    private Vine mapVineDtoToVine(VineDto vineDto) {
        Vine vine = new Vine();
        vine.setId(vineDto.getId());
        vine.setName(vineDto.getName());
        vine.setColor(vineDto.getColor());
        vine.setCountry(vineDto.getCountry());
        vine.setManufacturer(vineDto.getManufacturer());
        vine.setYear(vineDto.getYear());
        vine.setSugar(vineDto.getSugar());
        vine.setGrapeName(vineDto.getGrapeName());
        vine.setAmount(vineDto.getAmount());
        vine.setSoldWine(vineDto.getSoldWine());
        vine.setAbv(vineDto.getAbv());
        vine.setIsSparkling(vineDto.getIsSparkling());
        vine.setPrice(vineDto.getPrice());
        vine.setRegion(vineDto.getRegion());

        return vine;
    }

}
