package com.example.beerApp.controller;

import com.example.beerApp.dto.BaseResponse;
import com.example.beerApp.dto.beer.AddBeerRequest;
import com.example.beerApp.dto.beer.UpdateBeerRequest;
import com.example.beerApp.service.BeerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/beer")
@Validated
public class BeerController {
    private final BeerService beerService;

//    @GetMapping(path = "/getAllBeers")
////    @PreAuthorize("hasRole('client_admin')")
//    public ResponseEntity<BaseResponse> getAllBeers() {
//        return ResponseEntity.ok(createSuccessResponse(beerService.getAllBeers()));
//    }

    @GetMapping(path = "/getListOfBeers")
    public ResponseEntity<BaseResponse>getListOfBeers(
            @RequestParam(name = "username", required = false) String username){
        return ResponseEntity.ok(createSuccessResponse(beerService.getListOfBeers(username)));
    };

    @GetMapping(path = "/getOwnBeers")
    public ResponseEntity<BaseResponse> getOwnBeers() {
        return ResponseEntity.ok(createSuccessResponse(beerService.getOwnBeers()));
    }

    @PostMapping(path = "/add")
    public ResponseEntity<BaseResponse> addBeer(@Valid @RequestBody AddBeerRequest addBeerRequest) {
        return ResponseEntity.ok(createSuccessResponse(beerService.addBeer(addBeerRequest)));
    }

    @PutMapping(path = "/update/{beerId}")
    public ResponseEntity<BaseResponse> updateBeer(@Valid @RequestBody UpdateBeerRequest updateBeerRequest, @PathVariable String beerId) {
        return ResponseEntity.ok(createSuccessResponse(beerService.updateBeer(updateBeerRequest, beerId)));
    }

    @DeleteMapping(path = "/{beerId}")
    public ResponseEntity<BaseResponse> deleteBeer(@PathVariable String beerId) {
        beerService.deleteBeer(beerId);
        return ResponseEntity.ok((createSuccessResponse("Beer: " + beerId + " has been deleted")));
    }

    private BaseResponse createSuccessResponse(Object data) {
        return BaseResponse.builder().status("Success").data(data).build();
    }
}
