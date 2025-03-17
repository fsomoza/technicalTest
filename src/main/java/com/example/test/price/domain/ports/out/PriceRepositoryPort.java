package com.example.test.price.domain.ports.out;

import com.example.test.price.domain.models.Price;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepositoryPort {
    /**
     * Retrieves a list of prices for a given moment in time, product and brand
     *
     * @param dateTime      the application date
     * @param productId the product identifier
     * @param brandId   the brand (store) identifier
     * @return the applicable Price object, or null if none found
     */
    List<Price> findProductPricesAt(LocalDateTime dateTime, Long productId, Long brandId);
}
