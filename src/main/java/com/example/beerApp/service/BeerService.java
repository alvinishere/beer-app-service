package com.example.beerApp.service;

import com.example.beerApp.constant.ErrorMessage;
import com.example.beerApp.dto.AddBeerDto;
import com.example.beerApp.dto.BeerDto;
import com.example.beerApp.dto.BreweryDto;
import com.example.beerApp.dto.GetBeerDto;
import com.example.beerApp.dto.beer.AddBeerRequest;
import com.example.beerApp.dto.beer.UpdateBeerRequest;
import com.example.beerApp.dto.beer.UserBeerResponse;
import com.example.beerApp.entity.AuthUser;
import com.example.beerApp.entity.Beer;
import com.example.beerApp.exception.ResourceExistException;
import com.example.beerApp.exception.ResourceNotFoundException;
import com.example.beerApp.repository.AuthUserRepository;
import com.example.beerApp.repository.BeerRepository;
import com.example.beerApp.utility.UserUtil;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class BeerService {
    private final BeerRepository beerRepository;
    private final AuthUserRepository authUserRepository;
    private final UserUtil userUtil;
    private final BeerServiceHelper serviceHelper;
    private final AuthUserService authUserService;

    @Value("${jwt.auth.admin.role}")
    private String adminRole;

    public List<Beer> getListOfBeers(String username) {
        if (username == null) {
            return beerRepository.findAll();
        }
        UsersResource usersResource = authUserService.getUsersResource();
        UserRepresentation userRepresentation = usersResource.searchByUsername(username, true)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        AuthUser authUser = authUserRepository
                .findById(UUID.fromString(userRepresentation.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return beerRepository.findByAuthUsers(authUser);
//        return listOfBeers;
    }

    public List<Beer> getOwnBeers() {
        AuthUser user = authUserRepository.findById(UUID.fromString(userUtil.getUserId())).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return beerRepository.findByAuthUsers(user);
//        return serviceHelper.buildUserResponseList(foundBeers.stream());
    }


    public Beer addBeer(AddBeerRequest addBeerRequest) {
        String currentUserId = userUtil.getUserId();
        AuthUser foundUser = authUserRepository
                .findById(UUID.fromString(currentUserId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return beerRepository.save(serviceHelper.getBeerObject(addBeerRequest, foundUser));
    }

    public Beer updateBeer(UpdateBeerRequest updateBeerRequest, String beerId) {
        if (updateBeerRequest.isInValid()) {
            throw new BadRequestException("Invalid update request object");
        }
        Beer foundBeer = beerRepository
                .findById(UUID.fromString(beerId))
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE));
        if (!Objects.equals(foundBeer.getOwnerId(), userUtil.getUserId())) {
            throw new NotAuthorizedException(ErrorMessage.RESOURCE_DOES_NOT_BELONG_TO_USER);
        }
        if (updateBeerRequest.getName() != null) {
            foundBeer.setName(updateBeerRequest.getName());
        }
        if (updateBeerRequest.getScore() != null) {
            foundBeer.setScore(updateBeerRequest.getScore());
        }
        if (updateBeerRequest.getPrice() != null) {
            foundBeer.setPrice(updateBeerRequest.getPrice());
        }
        return beerRepository.save(foundBeer);
    }

    public void deleteBeer(String beerId) {
        UUID id = UUID.fromString(beerId);
        Beer beer = beerRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE));
        if (!Objects.equals(beer.getOwnerId(), userUtil.getUserId())) {
            throw new NotAuthorizedException(ErrorMessage.RESOURCE_DOES_NOT_BELONG_TO_USER);
        }
        beerRepository.deleteById(id);
    }
}
