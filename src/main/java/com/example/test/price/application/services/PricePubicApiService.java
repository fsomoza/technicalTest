package com.example.test.price.application.services;

import com.example.test.price.domain.exceptions.PriceNotFoundException;
import com.example.test.price.domain.models.Price;
import com.example.test.price.domain.ports.in.PricePublicApiPort;
import com.example.test.price.domain.ports.out.PriceRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class PricePubicApiService implements PricePublicApiPort {


    /*
     * In this service, two different approaches are demonstrated for finding the applicable price with the highest priority
     * for a given date, time, product ID, and brand ID.
     *
     * The first approach (`findApplicablePriceAtV1`) delegates the responsibility of selecting the highest priority price
     * to the database layer. By calling `findApplicablePriceAt` on the repository, it expects the repository to execute a
     * query that directly retrieves the price with the highest priority for the specified parameters. This method is
     * optimized for performance as it reduces the amount of data transferred and processed in the application layer.
     *
     * The second approach (`findApplicablePriceAtV2`) retrieves all applicable prices for the given date, product, and brand
     * from the repository and then uses Java streams to select the price with the highest priority. This approach adheres
     * more strictly to the principles of hexagonal architecture and the single responsibility principle, where the repository
     * is only responsible for data access, and the service layer encapsulates the business logic. While this may be less
     * efficient in scenarios with a large number of applicable prices, it offers better separation of concerns, which can
     * enhance maintainability and testability. Similar to the first approach, if there are multiple prices with the same
     * highest priority, the `max` operation will select one based on the natural ordering of the Price objects.
     *
     */

    private final PriceRepositoryPort priceRepositoryPort;

    @Autowired
    public PricePubicApiService(PriceRepositoryPort priceRepositoryPort) {
        this.priceRepositoryPort = priceRepositoryPort;
    }

    @Override
    public Price findApplicablePriceAtV1(LocalDateTime dateTime, Long productId, Long brandId) {

        return priceRepositoryPort.findApplicablePriceAt(dateTime, productId, brandId)
                .orElseThrow(() -> new PriceNotFoundException(productId, brandId));

    }

    @Override
    public Price findApplicablePriceAtV2(LocalDateTime dateTime, Long productId, Long brandId) {
        List<Price> priceList = priceRepositoryPort.findApplicablePricesAt(dateTime, productId, brandId);
        return priceList.stream()
                .max(Comparator.comparing(Price::getPriority))
                .orElseThrow(() -> new PriceNotFoundException(productId, brandId));
    }
}
