package com.mentorship.vineservice.dto;

import com.mentorship.vineservice.dto.enums.VineColor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VineDto {


    private Long id;

    @NotBlank(message = "Name can not be empty or null")
    @Length(max = 255, message = "Name name is too long")
    private String name;

    @NotBlank(message = "Manufacturer can not be empty or null")
    @Length(max = 255, message = "Manufacturer name is too long")
    private String manufacturer;

    @NotBlank(message = "Grape name can not be empty or null")
    @Length(max = 255, message = "Grape name name is too long")
    private String grapeName;

    @NotNull(message = "Color can not be null")
    private VineColor color;

    private Boolean isSparkling;

    @NotBlank(message = "Sugar can not be empty or null")
    @Length(max = 36, message = "Sugar name is too long")
    private String sugar;

    @NotBlank(message = "Country can not be empty or null")
    @Length(max = 255, message = "Country name is too long")
    private String country;

    @Length(max = 255, message = "Region name is too long")
    private String region;

    @Positive(message = "Year must be greater then 0")
    private Integer year;

    @Range(min = 0, max = 999999, message = "Price should be from 0 to 999999")
    private Double price;

    @Positive(message = "Amount must be greater then 0")
    private Integer amount;

    @Range(min = 0, max = 30, message = "Abv should be from 0 to 30")
    private Double abv;

    private Integer soldWine = 0;

}
