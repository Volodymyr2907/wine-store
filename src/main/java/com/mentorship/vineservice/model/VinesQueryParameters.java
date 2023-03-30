package com.mentorship.vineservice.model;

import java.util.Date;
import java.util.List;
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
    private String color;
    private String name;
    private String grapeName;
    private Integer year;
    private Integer page;
    private Integer size;
}
