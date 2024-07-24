package com.example.beerApp.dto.authUser;

public record SignUpRequest(String username, String email, String firstName, String lastName, String password) {
}
