package com.example.beerApp.exception;

public class ResourceExistException extends RuntimeException {
    public ResourceExistException() {
    }

    public ResourceExistException(String message) {
        super(message);
    }
}
