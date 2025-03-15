package com.example.test.price.domain.ports.in;

import com.example.test.price.domain.models.Price;

import java.time.LocalDateTime;

public interface PricePublicApiPort {
    /**
     * Finds the price for a product and brand at a given time.
     * If multiple rates overlap in their date ranges, the one with the highest priority is returned.
     *
     * @param time      the application date
     * @param productId the product identifier
     * @param brandId   the brand (store) identifier
     * @return the applicable Price object, or null if none found
     */
    Price findApplicablePriceAt(LocalDateTime time, Long productId, Long brandId);
}
