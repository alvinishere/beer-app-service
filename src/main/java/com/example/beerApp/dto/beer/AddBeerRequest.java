package com.example.beerApp.dto.beer;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AddBeerRequest(
        @NotBlank String name,
        @NotNull @Min(value = 1) @Max(value = 10) short score,
        @NotNull BigDecimal price) {
}
