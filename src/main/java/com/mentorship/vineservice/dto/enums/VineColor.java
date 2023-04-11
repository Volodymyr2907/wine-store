package com.mentorship.vineservice.dto.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public enum VineColor {

    WHITE,
    RED,
    ORANGE,
    ROSE;


    @JsonCreator
    public static VineColor getVineColor(String value) {
        for (VineColor vine : VineColor.values()) {
            if (vine.name().equals(value.toUpperCase())) {
                return vine;
            }
        }
        throw new IllegalArgumentException(
            "Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
    }
}
