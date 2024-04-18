package com.example.beerApp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private int status;
    private String message;
}
