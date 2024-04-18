package com.example.beerApp.controller;

import com.example.beerApp.dto.AddBeerDto;
import com.example.beerApp.dto.BeerDto;
import com.example.beerApp.dto.BeerResponse;
import com.example.beerApp.dto.GetBeerDto;
import com.example.beerApp.entity.Beer;
import com.example.beerApp.service.BeerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/beer")
@Validated
public class BeerController {
    private final BeerService beerService;

    @GetMapping(path = "/getAllBeers")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<BeerResponse> getAllBeers() {
        List<GetBeerDto> res = beerService.getAllBeers();
        return ResponseEntity.ok(createSuccessResponse(res));
    }

    @GetMapping(path = "/getMyBeers")
    public ResponseEntity<BeerResponse> getMyBeers() {
        List<GetBeerDto> res = beerService.getMyBeers();
        return ResponseEntity.ok(createSuccessResponse(res));
    }

    @GetMapping(path = "/getAllUserBeers/{username}")
    public ResponseEntity<BeerResponse> getAllUserBeers(@PathVariable String username) {
        List<GetBeerDto> res = beerService.getAllUserBeers(username);
        return ResponseEntity.ok(createSuccessResponse(res));
    }


    @PostMapping(path = "/add")
    public ResponseEntity<BeerResponse> addBeer(@Valid @RequestBody AddBeerDto addBeerDto) {
        Beer res = beerService.addBeer(addBeerDto);
        return ResponseEntity.ok(createSuccessResponse(res));
    }

    @PutMapping(path = "/update/{beerId}")
    public ResponseEntity<BeerResponse> updateBeer(@Valid @RequestBody BeerDto updateBeerDto, @PathVariable String beerId) {
        Beer res = beerService.updateBeer(updateBeerDto, beerId);
        return ResponseEntity.ok(createSuccessResponse(res));
    }

    @DeleteMapping(path = "/{beerId}")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<BeerResponse> deleteBeer(@PathVariable String beerId) {
        beerService.deleteBeer(beerId);
        return ResponseEntity.ok((createSuccessResponse("Beer: " + beerId + " has been deleted")));
    }

    private BeerResponse createSuccessResponse(Object data) {
        return BeerResponse.builder().status("Success").data(data).build();
    }
}
