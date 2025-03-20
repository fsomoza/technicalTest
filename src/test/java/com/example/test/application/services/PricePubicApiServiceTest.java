package com.example.test.application.services;

import com.example.test.price.application.services.PricePubicApiService;
import com.example.test.price.domain.exceptions.PriceNotFoundException;
import com.example.test.price.domain.models.Price;
import com.example.test.price.domain.ports.out.PriceRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PricePubicApiServiceTest {

    @Mock
    private PriceRepositoryPort priceRepositoryPort;

    @InjectMocks
    private PricePubicApiService pricePubicApiService;

    private LocalDateTime testDateTime;
    private Long testProductId;
    private Long testBrandId;
    private Price testPrice;
    private List<Price> testPrices;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        testProductId = 35455L;
        testBrandId = 1L;

        // Create a test price
        testPrice = new Price(
                testBrandId,
                LocalDateTime.of(2020, 6, 1, 0, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59, 59),
                1L,
                testProductId,
                0,
                new BigDecimal("35.50"),
                "EUR"
        );

        // Create a list of test prices with different priorities
        testPrices = new ArrayList<>();
        testPrices.add(testPrice);
        testPrices.add(new Price(
                testBrandId,
                LocalDateTime.of(2020, 6, 14, 15, 0, 0),
                LocalDateTime.of(2020, 6, 14, 18, 30, 0),
                2L,
                testProductId,
                1, // Higher priority
                new BigDecimal("25.45"),
                "EUR"
        ));
    }

    @Test
    void findApplicablePriceAtV1_ShouldReturnPrice_WhenPriceExists() {
        when(priceRepositoryPort.findApplicablePriceAt(eq(testDateTime), eq(testProductId), eq(testBrandId)))
                .thenReturn(Optional.of(testPrice));

        Price result = pricePubicApiService.findApplicablePriceAtV1(testDateTime, testProductId, testBrandId);

        assertNotNull(result);
        assertEquals(testPrice.getBrandId(), result.getBrandId());
        assertEquals(testPrice.getProductId(), result.getProductId());
        assertEquals(testPrice.getPriceList(), result.getPriceList());
        assertEquals(testPrice.getPrice(), result.getPrice());
    }

    @Test
    void findApplicablePriceAtV1_ShouldThrowException_WhenNoPriceExists() {
        when(priceRepositoryPort.findApplicablePriceAt(eq(testDateTime), eq(testProductId), eq(testBrandId)))
                .thenReturn(Optional.empty());

        PriceNotFoundException exception = assertThrows(PriceNotFoundException.class, () -> {
            pricePubicApiService.findApplicablePriceAtV1(testDateTime, testProductId, testBrandId);
        });

        assertTrue(exception.getMessage().contains("No applicable price found for product ID " + testProductId));
        assertTrue(exception.getMessage().contains("brand ID " + testBrandId));
    }

    @Test
    void findApplicablePriceAtV2_ShouldReturnHighestPriorityPrice_WhenMultiplePricesExist() {
        when(priceRepositoryPort.findApplicablePricesAt(eq(testDateTime), eq(testProductId), eq(testBrandId)))
                .thenReturn(testPrices);

        Price result = pricePubicApiService.findApplicablePriceAtV2(testDateTime, testProductId, testBrandId);

        assertNotNull(result);
        // Should return the price with priority 1 (the second in our test list)
        assertEquals(2L, result.getPriceList());
        assertEquals(new BigDecimal("25.45"), result.getPrice());
        assertEquals(1, result.getPriority());
    }

    @Test
    void findApplicablePriceAtV2_ShouldThrowException_WhenNoPricesExist() {
        when(priceRepositoryPort.findApplicablePricesAt(eq(testDateTime), eq(testProductId), eq(testBrandId)))
                .thenReturn(new ArrayList<>());

        PriceNotFoundException exception = assertThrows(PriceNotFoundException.class, () -> {
            pricePubicApiService.findApplicablePriceAtV2(testDateTime, testProductId, testBrandId);
        });

        assertTrue(exception.getMessage().contains("No applicable price found for product ID " + testProductId));
        assertTrue(exception.getMessage().contains("brand ID " + testBrandId));
    }

    @Test
    void findApplicablePriceAtV2_ShouldReturnSinglePrice_WhenOnlyOnePriceExists() {

        List<Price> singlePriceList = new ArrayList<>();
        singlePriceList.add(testPrice);

        when(priceRepositoryPort.findApplicablePricesAt(eq(testDateTime), eq(testProductId), eq(testBrandId)))
                .thenReturn(singlePriceList);

        Price result = pricePubicApiService.findApplicablePriceAtV2(testDateTime, testProductId, testBrandId);


        assertNotNull(result);
        assertEquals(testPrice.getPriceList(), result.getPriceList());
        assertEquals(testPrice.getPrice(), result.getPrice());
    }
}