package com.example.beerApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BreweryDto implements Serializable {
    @JsonProperty("id")
    private String breweryId;
    @JsonProperty("name")
    private String breweryName;
    @JsonProperty("brewery_type")
    private String breweryType;
}
