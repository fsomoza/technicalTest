package com.example.test.price.infrastructure.adapters.httpapi;

import com.example.test.price.domain.models.Price;
import com.example.test.price.domain.ports.in.PricePublicApiPort;
import com.example.test.price.infrastructure.adapters.httpapi.dtos.PriceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    private final PricePublicApiPort pricePublicApiPort;

    @Autowired
    public PriceController(PricePublicApiPort pricePublicApiPort) {
        this.pricePublicApiPort = pricePublicApiPort;
    }

    @GetMapping("/v1")
    public ResponseEntity<PriceResponse> getApplicablePriceV1(
            @RequestParam("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            @RequestParam("productId") Long productId,
            @RequestParam("brandId") Long brandId) {

        Price price = pricePublicApiPort.findApplicablePriceAtV1(dateTime, productId, brandId);

        return ResponseEntity.ok(PriceResponse.fromDomainModel(price));
    }

    @GetMapping("/v2")
    public ResponseEntity<PriceResponse> getApplicablePriceV2(
            @RequestParam("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            @RequestParam("productId") Long productId,
            @RequestParam("brandId") Long brandId) {

        Price price = pricePublicApiPort.findApplicablePriceAtV2(dateTime, productId, brandId);

        return ResponseEntity.ok(PriceResponse.fromDomainModel(price));
    }


}