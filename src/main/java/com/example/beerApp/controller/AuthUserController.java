package com.example.beerApp.controller;

import com.example.beerApp.dto.BaseResponse;
import com.example.beerApp.dto.authUser.LoginRequest;
import com.example.beerApp.dto.authUser.SignUpRequest;
import com.example.beerApp.dto.authUser.AuthUserResponse;
import com.example.beerApp.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/auth")
public class AuthUserController {
    private final AuthUserService authUserService;

    @PostMapping(path = "/login")
    public ResponseEntity<BaseResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(
                createSuccessResponse(authUserService.loginUser(loginRequest)));
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<BaseResponse> signUpUser(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createSuccessResponse(authUserService.signUpUser(signUpRequest)));
    }

    private BaseResponse createSuccessResponse(Object data) {
        return BaseResponse.builder().status("Success").data(data).build();
    }
}
