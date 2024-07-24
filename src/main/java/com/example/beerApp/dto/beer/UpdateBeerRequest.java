package com.example.beerApp.dto.beer;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateBeerRequest {
    private String name;
//    @Min(value = 1)
//    @Max(value = 10)
    private Short score;
    private BigDecimal price;

    public boolean isInValid() {
        return name == null && score == null &&price == null;
    }
}
