package com.example.beerApp.service;

import com.example.beerApp.dto.BreweryDto;
import com.example.beerApp.dto.GetBeerDto;
import com.example.beerApp.entity.Beer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RequiredArgsConstructor
@Component
public class BeerServiceHelper {
    private final RestTemplate restTemplate;
    @Value("${brewery.api.baseurl}")
    private String breweryBaseUrl;

    public List<BreweryDto> getAllBreweryByIds(List<String> ids) {
        Map<String, String> params = new HashMap<>();
        String paramValue = "";
        for (String id : ids) {
            if (paramValue.isEmpty()) {
                paramValue = id;
            } else {
                paramValue = String.join(",", paramValue, id);
            }
        }
        params.put("by_ids", paramValue);
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(breweryBaseUrl + "?by_ids={by_ids}", BreweryDto[].class, params))).toList();
    }

    public Map<String, BreweryDto> getBreweryMap(List<BreweryDto> breweries) {
        Map<String, BreweryDto> breweryMap = new HashMap<>();
        for (BreweryDto brewery : breweries) {
            breweryMap.put(brewery.getBreweryId(), brewery);
        }
        return breweryMap;
    }

    public List<GetBeerDto> getBeerDtosList(List<Beer> beers) {
        List<String> listOfIds = beers.stream().map(Beer::getBreweryId).toList();
        List<BreweryDto> breweries = getAllBreweryByIds(listOfIds);
        Map<String, BreweryDto> breweryMap = getBreweryMap(breweries);
        return beers.stream().map(beer -> {
            BreweryDto foundBreweryInfo = breweryMap.get(beer.getBreweryId());
            GetBeerDto getBeerDto = GetBeerDto.builder()
                    .id(beer.getId())
                    .name(beer.getName())
                    .price(beer.getPrice())
                    .score(beer.getScore())
                    .username(beer.getUsername())
                    .breweryId(beer.getBreweryId())
                    .build();
            if (foundBreweryInfo != null) {
                getBeerDto.setBreweryName(foundBreweryInfo.getBreweryName());
                getBeerDto.setBreweryType(foundBreweryInfo.getBreweryType());
            }
            return getBeerDto;
        }).toList();
    }
}
