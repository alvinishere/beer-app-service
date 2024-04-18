package com.example.beerApp.dto;

import com.example.beerApp.entity.Beer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BeerResponse {
    private String status;
    private Object data;
}
