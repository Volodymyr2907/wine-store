package com.mentorship.vineservice.mapper;

import com.mentorship.vineservice.domain.Vine;
import com.mentorship.vineservice.dto.VineDto;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VineMapper {

    Vine mapVine(VineDto vineDto);

    List<VineDto> mapVinesDto(List<Vine> vines);

}
