package com.example.test.price.infrastructure.adapters.repositories.jparepository;

import com.example.test.price.infrastructure.adapters.repositories.jparepository.entities.JpaPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JpaPriceRepository extends JpaRepository<JpaPriceEntity, Long> {

    /**
     * Find the price with the highest priority applicable at a specific date time for a product and brand
     *
     * @param dateTime the date and time to check
     * @param productId the product ID
     * @param brandId the brand ID
     * @return an Optional containing the applicable PriceEntity if one exists, or an empty Optional if none is found
     */
    @Query("SELECT p FROM JpaPriceEntity p " +
            "WHERE p.productId = :productId " +
            "AND p.brandId = :brandId " +
            "AND :dateTime BETWEEN p.startDate AND p.endDate " +
            "ORDER BY p.priority DESC LIMIT 1")
    Optional<JpaPriceEntity> findApplicablePriceAt(
            @Param("dateTime") LocalDateTime dateTime,
            @Param("productId") Long productId,
            @Param("brandId") Long brandId);

    /**
     * Retrieves a list of prices at a specific date time for a product and brand
     *
     * @param dateTime the date and time to check
     * @param productId the product ID
     * @param brandId the brand ID
     * @return the list of prices that are applicable, if none results an empty list
     */
    @Query("SELECT p FROM JpaPriceEntity p " +
            "WHERE p.productId = :productId " +
            "AND p.brandId = :brandId " +
            "AND :dateTime BETWEEN p.startDate AND p.endDate")
    List<JpaPriceEntity> findApplicablePricesAt(
            @Param("dateTime") LocalDateTime dateTime,
            @Param("productId") Long productId,
            @Param("brandId") Long brandId);
}