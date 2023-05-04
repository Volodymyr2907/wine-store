package com.mentorship.vineservice.model;

import com.mentorship.vineservice.dto.enums.VineColor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class VinesQueryParameters {

    private String sugar;
    private VineColor color;
    private String name;
    private String grapeName;
    private Integer year;
    private Integer page;
    private Integer size;
}
