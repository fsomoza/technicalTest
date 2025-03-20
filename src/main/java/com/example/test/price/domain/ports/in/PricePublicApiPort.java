package com.example.test.price.domain.ports.in;
import com.example.test.price.domain.models.Price;
import java.time.LocalDateTime;

public interface PricePublicApiPort {
    /**
     * Finds the price for a product and brand at a given time.
     * If multiple rates overlap in their date ranges, the one with the highest priority is returned.
     *
     * @param dateTime      the application date and time
     * @param productId the product identifier
     * @param brandId   the brand (store) identifier
     * @return the applicable Price object
     */
    Price findApplicablePriceAtV1(LocalDateTime dateTime, Long productId, Long brandId);

    /**
     * Finds the price for a product and brand at a given time.
     * If multiple rates overlap in their date ranges, the one with the highest priority is returned.
     *
     * @param dateTime      the application date and time
     * @param productId the product identifier
     * @param brandId   the brand (store) identifier
     * @return the applicable Price object
     */
     Price findApplicablePriceAtV2(LocalDateTime dateTime, Long productId, Long brandId);
}
