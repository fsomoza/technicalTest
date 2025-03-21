package com.example.test.price.infrastructure.adapters.repositories;

import com.example.test.price.domain.models.Price;
import com.example.test.price.domain.ports.out.PriceRepositoryPort;
import com.example.test.price.infrastructure.adapters.repositories.jparepository.JpaPriceRepository;
import com.example.test.price.infrastructure.adapters.repositories.jparepository.entities.JpaPriceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PriceRepositoryAdapter implements PriceRepositoryPort {
    private final JpaPriceRepository jpaPriceRepository;

    @Autowired
    public PriceRepositoryAdapter(JpaPriceRepository jpaPriceRepository) {
        this.jpaPriceRepository = jpaPriceRepository;
    }

    @Override
    public Optional<Price> findApplicablePriceAt(LocalDateTime dateTime, Long productId, Long brandId) {
        Optional<JpaPriceEntity> priceEntity = jpaPriceRepository.findApplicablePriceAt(dateTime, productId, brandId);
        return priceEntity.map(JpaPriceEntity::toDomainModel);
        //dlksajflkdsjf
    }

    @Override
    public List<Price> findApplicablePricesAt(LocalDateTime dateTime, Long productId, Long brandId) {
        List<JpaPriceEntity> entities = jpaPriceRepository.findApplicablePricesAt(dateTime, productId, brandId);

        return entities.stream()
                .map(JpaPriceEntity::toDomainModel)
                .collect(Collectors.toList());
    }
}
