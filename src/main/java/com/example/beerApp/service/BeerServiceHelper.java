package com.example.beerApp.service;

import com.example.beerApp.dto.BreweryDto;
import com.example.beerApp.dto.GetBeerDto;
import com.example.beerApp.dto.beer.AddBeerRequest;
import com.example.beerApp.dto.beer.UserBeerResponse;
import com.example.beerApp.entity.AuthUser;
import com.example.beerApp.entity.Beer;
import com.example.beerApp.repository.BeerRepository;
import com.example.beerApp.utility.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class BeerServiceHelper {
    private final BeerRepository beerRepository;
    private final UserUtil userUtil;
//    private final RestTemplate restTemplate;
//    @Value("${brewery.api.baseurl}")
//    private final String breweryBaseUrl;

//    public List<BreweryDto> getAllBreweryByIds(List<String> ids) {
//        Map<String, String> params = new HashMap<>();
//        String paramValue = "";
//        for (String id : ids) {
//            if (paramValue.isEmpty()) {
//                paramValue = id;
//            } else {
//                paramValue = String.join(",", paramValue, id);
//            }
//        }
//        params.put("by_ids", paramValue);
//        return Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(breweryBaseUrl + "?by_ids={by_ids}", BreweryDto[].class, params))).toList();
//    }

//    public Map<String, BreweryDto> getBreweryMap(List<BreweryDto> breweries) {
//        Map<String, BreweryDto> breweryMap = new HashMap<>();
//        for (BreweryDto brewery : breweries) {
//            breweryMap.put(brewery.getBreweryId(), brewery);
//        }
//        return breweryMap;
//    }

    public List<GetBeerDto> getBeerDtosList(List<Beer> beers) {
//        List<String> listOfIds = beers.stream().map(Beer::getBreweryId).toList();
//        List<BreweryDto> breweries = getAllBreweryByIds(listOfIds);
//        Map<String, BreweryDto> breweryMap = getBreweryMap(breweries);
        return beers.stream().map(beer -> {
//            BreweryDto foundBreweryInfo = breweryMap.get(beer.getBreweryId());
            GetBeerDto getBeerDto = GetBeerDto.builder()
                    .id(beer.getId())
                    .name(beer.getName())
                    .price(beer.getPrice())
                    .score(beer.getScore())
//                    .breweryId(beer.getBreweryId())
                    .build();
//            if (foundBreweryInfo != null) {
//                getBeerDto.setBreweryName(foundBreweryInfo.getBreweryName());
//                getBeerDto.setBreweryType(foundBreweryInfo.getBreweryType());
//            }
            return getBeerDto;
        }).toList();
    }

    public Beer getBeerObject(AddBeerRequest addBeerRequest, AuthUser foundUser) {
        Beer beer = beerRepository.findByName(addBeerRequest.name())
                .orElseGet(() -> Beer.builder()
                        .name(addBeerRequest.name())
                        .score(addBeerRequest.score())
                        .price(addBeerRequest.price())
                        .ownerId(userUtil.getUserId())
                        .build());
        Set<AuthUser> users = new HashSet<>();
        if(beer.getAuthUsers() != null){
            users.addAll(beer.getAuthUsers());
        }
        users.add(foundUser);
        beer.setAuthUsers(users);
        return beer;
    }

    public List<UserBeerResponse> buildUserResponseList(Stream<Beer> beers) {
        return beers.map(beer -> UserBeerResponse.builder()
                .id(beer.getId())
                .name(beer.getName())
                .score(beer.getScore())
                .price(beer.getPrice())
                .breweryId(beer.getBreweryId())
                .build()).toList();
    }
}
