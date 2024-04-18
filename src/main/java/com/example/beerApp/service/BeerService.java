package com.example.beerApp.service;

import com.example.beerApp.constant.ErrorMessage;
import com.example.beerApp.dto.AddBeerDto;
import com.example.beerApp.dto.BeerDto;
import com.example.beerApp.dto.BreweryDto;
import com.example.beerApp.dto.GetBeerDto;
import com.example.beerApp.entity.Beer;
import com.example.beerApp.exception.ResourceExistException;
import com.example.beerApp.exception.ResourceNotFoundException;
import com.example.beerApp.repository.BeerRepository;
import com.example.beerApp.utility.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RequiredArgsConstructor
@Service
public class BeerService {
    private final BeerRepository beerRepository;
    private final UserUtil userUtil;
    private final BeerServiceHelper serviceHelper;

    @Value("${jwt.auth.admin.role}")
    private String adminRole;

    public List<GetBeerDto> getAllBeers() {
        List<Beer> allBeers = beerRepository.findAll();
        return serviceHelper.getBeerDtosList(allBeers);
    }

    public List<GetBeerDto> getMyBeers() {
        List<Beer> beers = beerRepository.findAllByUsername(userUtil.getUserName());
        return serviceHelper.getBeerDtosList(beers);
    }

    public List<GetBeerDto> getAllUserBeers(String username) {
        List<Beer> beers = beerRepository.findAllByUsername(username);
        return serviceHelper.getBeerDtosList(beers);
    }

    public Beer addBeer(AddBeerDto addBeerDto) {
        if (beerRepository.findByName(addBeerDto.getName()).isPresent()) {
            throw new ResourceExistException(ErrorMessage.RESOURCE_ALREADY_EXISTS_MESSAGE);
        }
        Beer newBeer = Beer.builder().name(addBeerDto.getName()).price(addBeerDto.getPrice()).score(addBeerDto.getScore()).username(userUtil.getUserName()).breweryId(addBeerDto.getBreweryId()).build();
        return beerRepository.save(newBeer);
    }

    public Beer updateBeer(BeerDto updateBeerDto, String beerId) {
        Beer beer = beerRepository.findById(UUID.fromString(beerId)).orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE));
        if (!userUtil.getRoles().contains(adminRole) && !Objects.equals(userUtil.getUserName(), beer.getUsername())) {
            throw new RuntimeException(ErrorMessage.RESOURCE_DOES_NOT_BELONG_TO_USER);
        }
        beer.setName(updateBeerDto.getName());
        beer.setScore(updateBeerDto.getScore());
        return beerRepository.save(beer);
    }

    public void deleteBeer(String beerId) {
        UUID id = UUID.fromString(beerId);
        if (beerRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE);
        }
        beerRepository.deleteById(id);
    }
}
