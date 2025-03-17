package com.example.test.price.infrastructure.adapters.repositories.jparepository;

import com.example.test.price.infrastructure.adapters.repositories.jparepository.entities.JpaPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPriceRepository extends JpaRepository<JpaPriceEntity, Long> {
}
