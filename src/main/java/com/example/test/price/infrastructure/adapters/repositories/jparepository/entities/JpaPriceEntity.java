package com.example.test.price.infrastructure.adapters.repositories.jparepository.entities;


import com.example.test.price.domain.models.Price;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "price")
public class JpaPriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long brandId;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    @Column
    private Long priceList;

    @Column
    private Long productId;

    @Column
    private Integer priority;

    @Column
    private BigDecimal price;

    @Column
    private String currency;

    protected JpaPriceEntity() {}

    public Long getId() {
        return id;
    }

    public Long getBrandId() {
        return brandId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Long getPriceList() {
        return priceList;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getPriority() {
        return priority;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }


    public Price toDomainModel() {
        return new Price(
                this.brandId,
                this.startDate,
                this.endDate,
                this.priceList,
                this.productId,
                this.priority,
                this.price,
                this.currency
        );
    }

}