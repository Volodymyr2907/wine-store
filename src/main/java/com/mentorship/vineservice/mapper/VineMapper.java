package com.mentorship.vineservice.mapper;

import com.mentorship.vineservice.domain.Vine;
import com.mentorship.vineservice.dto.VineDto;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VineMapper {

    Vine vineDtoToVine(VineDto vineDto);

    List<VineDto> vineListToVineDtoList(List<Vine> vines);

}
