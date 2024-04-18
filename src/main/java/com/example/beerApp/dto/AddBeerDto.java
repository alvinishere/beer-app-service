package com.example.beerApp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AddBeerDto extends BeerDto{
    @NotNull
    private BigDecimal price;

    @NotNull
    @NotBlank
    private String breweryId;
}
