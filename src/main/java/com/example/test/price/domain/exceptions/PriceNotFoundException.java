package com.example.test.price.domain.exceptions;

public class PriceNotFoundException extends RuntimeException {

    public PriceNotFoundException(String message) {
        super(message);
    }

    public PriceNotFoundException(Long productId, Long brandId) {
        super(String.format("No applicable price found for product ID %d and brand ID %d", productId, brandId));
    }
}