package com.example.beerApp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BeerDto {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    private short score;
}
