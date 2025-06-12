package com.example.priceapi.infrastructure.adapter.in.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
