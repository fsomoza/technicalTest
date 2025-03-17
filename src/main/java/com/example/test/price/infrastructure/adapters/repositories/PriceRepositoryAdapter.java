package com.example.test.price.infrastructure.adapters.repositories;

import com.example.test.price.domain.models.Price;
import com.example.test.price.domain.ports.out.PriceRepositoryPort;

import java.time.LocalDateTime;
import java.util.List;

public class PriceRepositoryAdapter implements PriceRepositoryPort {
    @Override
    public List<Price> findProductPricesAt(LocalDateTime dateTime, Long productId, Long brandId) {
        return List.of();
    }
}
