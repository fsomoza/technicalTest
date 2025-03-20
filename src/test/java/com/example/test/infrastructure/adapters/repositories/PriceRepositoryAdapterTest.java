package com.example.test.infrastructure.adapters.repositories;

import com.example.test.price.domain.models.Price;
import com.example.test.price.infrastructure.adapters.repositories.PriceRepositoryAdapter;
import com.example.test.price.infrastructure.adapters.repositories.jparepository.JpaPriceRepository;
import com.example.test.price.infrastructure.adapters.repositories.jparepository.entities.JpaPriceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
public class PriceRepositoryAdapterTest {

    @Mock
    private JpaPriceRepository jpaPriceRepository;

    @InjectMocks
    private PriceRepositoryAdapter priceRepositoryAdapter;

    private LocalDateTime testDateTime;
    private Long testProductId;
    private Long testBrandId;
    private JpaPriceEntity testPriceEntity;
    private List<JpaPriceEntity> testPriceEntities;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        testProductId = 35455L;
        testBrandId = 1L;

        // Create a mock price entity instead of a real instance
        testPriceEntity = Mockito.mock(JpaPriceEntity.class);

        // Create a list of test price entities
        testPriceEntities = new ArrayList<>();
        testPriceEntities.add(testPriceEntity);
    }

    @Test
    void findApplicablePriceAt_ShouldReturnPrice_WhenPriceExists() {

        Price expectedPrice = new Price(
                testBrandId,
                LocalDateTime.of(2020, 6, 1, 0, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59, 59),
                1L,
                testProductId,
                0,
                new BigDecimal("35.50"),
                "EUR"
        );

        // Mock the repository to return our mocked entity
        when(jpaPriceRepository.findApplicablePriceAt(eq(testDateTime), eq(testProductId), eq(testBrandId)))
                .thenReturn(Optional.of(testPriceEntity));

        // Mock the entity's toDomainModel method to return our expected price
        when(testPriceEntity.toDomainModel()).thenReturn(expectedPrice);


        Optional<Price> result = priceRepositoryAdapter.findApplicablePriceAt(testDateTime, testProductId, testBrandId);

        assertTrue(result.isPresent());
        Price price = result.get();
        assertEquals(testBrandId, price.getBrandId());
        assertEquals(testProductId, price.getProductId());
        assertEquals(1L, price.getPriceList());
        assertEquals(new BigDecimal("35.50"), price.getPrice());
        assertEquals("EUR", price.getCurrency());
    }

    @Test
    void findApplicablePriceAt_ShouldReturnEmpty_WhenNoPriceExists() {
        when(jpaPriceRepository.findApplicablePriceAt(eq(testDateTime), eq(testProductId), eq(testBrandId)))
                .thenReturn(Optional.empty());

        Optional<Price> result = priceRepositoryAdapter.findApplicablePriceAt(testDateTime, testProductId, testBrandId);

        assertFalse(result.isPresent());
    }

    @Test
    void findApplicablePricesAt_ShouldReturnPrices_WhenPricesExist() {

        Price expectedPrice = new Price(
                testBrandId,
                LocalDateTime.of(2020, 6, 1, 0, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59, 59),
                1L,
                testProductId,
                0,
                new BigDecimal("35.50"),
                "EUR"
        );

        // Mock the repository to return our list of mocked entities
        when(jpaPriceRepository.findApplicablePricesAt(eq(testDateTime), eq(testProductId), eq(testBrandId)))
                .thenReturn(testPriceEntities);

        // Mock the entity's toDomainModel method to return our expected price
        when(testPriceEntity.toDomainModel()).thenReturn(expectedPrice);

        List<Price> result = priceRepositoryAdapter.findApplicablePricesAt(testDateTime, testProductId, testBrandId);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        Price price = result.get(0);
        assertEquals(testBrandId, price.getBrandId());
        assertEquals(testProductId, price.getProductId());
        assertEquals(1L, price.getPriceList());
        assertEquals(new BigDecimal("35.50"), price.getPrice());
        assertEquals("EUR", price.getCurrency());
    }

    @Test
    void findApplicablePricesAt_ShouldReturnEmptyList_WhenNoPricesExist() {

        when(jpaPriceRepository.findApplicablePricesAt(eq(testDateTime), eq(testProductId), eq(testBrandId)))
                .thenReturn(new ArrayList<>());

        List<Price> result = priceRepositoryAdapter.findApplicablePricesAt(testDateTime, testProductId, testBrandId);

        assertTrue(result.isEmpty());
    }
}