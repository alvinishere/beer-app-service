package com.example.beerApp.dto;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GetBeerDto {
    private UUID id;
    private String name;
    private BigDecimal price;
    private short score;
    private String username;
    private String breweryId;
    private String breweryName;
    private String breweryType;
}
