package com.example.beerApp.dto.beer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserBeerResponse {
    private UUID id;
    private String name;
    private BigDecimal price;
    private short score;
    private String breweryId;
    private boolean liked;
}
