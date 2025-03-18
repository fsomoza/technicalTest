package com.example.test.price.domain.ports.out;

import com.example.test.price.domain.models.Price;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PriceRepositoryPort {
    /**
     * Retrieves the price for a given moment in time, product and brand
     * If multiple rates overlap in their date ranges, the one with the highest priority is returned.
     *
     * @param dateTime      the application date and time
     * @param productId the product identifier
     * @param brandId   the brand (store) identifier
     * @return an Optional containing the applicable Price if one exists, or an empty Optional if none is found
     */
    Optional<Price> findApplicablePriceAt(LocalDateTime dateTime, Long productId, Long brandId);


    /**
     * Retrieves a list of prices for a given moment in time, product and brand
     *
     * @param dateTime      the application date and time
     * @param productId the product identifier
     * @param brandId   the brand (store) identifier
     * @return the list of applicable price objects, empty if none found
     */
    List<Price> findApplicablePricesAt(LocalDateTime dateTime, Long productId, Long brandId);
}
