package com.example.test.infrastructure.adapters.httapi;

import com.example.test.price.infrastructure.adapters.httpapi.dtos.ErrorResponse;
import com.example.test.price.infrastructure.adapters.httpapi.dtos.PriceResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the PriceController.
 * These tests make actual HTTP requests to the API endpoints.
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.profiles.active=test"
)
public class PriceControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    private String buildUrl(String version, LocalDateTime dateTime, Long productId, Long brandId) {
        String formattedDateTime = formatDateTime(dateTime);
        return "http://localhost:" + port + "/api/prices/" + version +
                "?dateTime=" + formattedDateTime +
                "&productId=" + productId +
                "&brandId=" + brandId;
    }


    // Tests for v1 endpoint
    @Test
    public void testGetApplicablePriceV1_Case1() {
        // Test case 1: Request at 2020-06-14 10:00:00 for product 35455 and brand 1
        // Expected: Price List 1 (35.50 EUR)
        LocalDateTime dateTime = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        String url = buildUrl("v1", dateTime, 35455L, 1L);

        ResponseEntity<PriceResponse> response = restTemplate.getForEntity(url, PriceResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getProductId()).isEqualTo(35455L);
        assertThat(response.getBody().getBrandId()).isEqualTo(1L);
        assertThat(response.getBody().getPriceList()).isEqualTo(1L);
        assertThat(response.getBody().getPrice().doubleValue()).isEqualTo(35.50);
        assertThat(response.getBody().getCurrency()).isEqualTo("EUR");
    }

    @Test
    public void testGetApplicablePriceV1_Case2() {
        // Test case 2: Request at 2020-06-14 16:00:00 for product 35455 and brand 1
        // Expected: Price List 2 (25.45 EUR)
        LocalDateTime dateTime = LocalDateTime.of(2020, 6, 14, 16, 0, 0);
        String url = buildUrl("v1", dateTime, 35455L, 1L);

        ResponseEntity<PriceResponse> response = restTemplate.getForEntity(url, PriceResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPriceList()).isEqualTo(2L);
        assertThat(response.getBody().getPrice().doubleValue()).isEqualTo(25.45);
    }

    @Test
    public void testGetApplicablePriceV1_Case3() {
        // Test case 3: Request at 2020-06-14 21:00:00 for product 35455 and brand 1
        // Expected: Price List 1 (35.50 EUR)
        LocalDateTime dateTime = LocalDateTime.of(2020, 6, 14, 21, 0, 0);
        String url = buildUrl("v1", dateTime, 35455L, 1L);

        ResponseEntity<PriceResponse> response = restTemplate.getForEntity(url, PriceResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPriceList()).isEqualTo(1L);
        assertThat(response.getBody().getPrice().doubleValue()).isEqualTo(35.50);
    }

    @Test
    public void testGetApplicablePriceV1_Case4() {
        // Test case 4: Request at 2020-06-15 10:00:00 for product 35455 and brand 1
        // Expected: Price List 3 (30.50 EUR)
        LocalDateTime dateTime = LocalDateTime.of(2020, 6, 15, 10, 0, 0);
        String url = buildUrl("v1", dateTime, 35455L, 1L);

        ResponseEntity<PriceResponse> response = restTemplate.getForEntity(url, PriceResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPriceList()).isEqualTo(3L);
        assertThat(response.getBody().getPrice().doubleValue()).isEqualTo(30.50);
    }

    @Test
    public void testGetApplicablePriceV1_Case5() {
        // Test case 5: Request at 2020-06-16 21:00:00 for product 35455 and brand 1
        // Expected: Price List 4 (38.95 EUR)
        LocalDateTime dateTime = LocalDateTime.of(2020, 6, 16, 21, 0, 0);
        String url = buildUrl("v1", dateTime, 35455L, 1L);

        ResponseEntity<PriceResponse> response = restTemplate.getForEntity(url, PriceResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPriceList()).isEqualTo(4L);
        assertThat(response.getBody().getPrice().doubleValue()).isEqualTo(38.95);
    }

    @Test
    public void testGetApplicablePriceV1_ProductNotFound() {
        // Test case 6: Request with a product that doesn't exist
        LocalDateTime dateTime = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        String url = buildUrl("v1", dateTime, 99999L, 1L);

        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(url, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("No applicable price found for product ID 99999");
    }

    @Test
    public void testGetApplicablePriceV1_BrandNotFound() {
        // Test case 7: Request with a brand that doesn't exist
        LocalDateTime dateTime = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        String url = buildUrl("v1", dateTime, 35455L, 99L);

        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(url, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("No applicable price found for product ID 35455 and brand ID 99");
    }

    @Test
    public void testGetApplicablePriceV1_DateOutOfRange() {
        // Test case 8: Request with a date outside of the range
        LocalDateTime dateTime = LocalDateTime.of(2019, 6, 14, 10, 0, 0);
        String url = buildUrl("v1", dateTime, 35455L, 1L);

        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(url, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("No applicable price found for product ID 35455 and brand ID 1");
    }

    // Tests for v2 endpoint

    @Test
    public void testGetApplicablePriceV2_Case1() {
        // Test case 1: Request at 2020-06-14 10:00:00 for product 35455 and brand 1
        // Expected: Price List 1 (35.50 EUR)
        LocalDateTime dateTime = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        String url = buildUrl("v2", dateTime, 35455L, 1L);

        ResponseEntity<PriceResponse> response = restTemplate.getForEntity(url, PriceResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getProductId()).isEqualTo(35455L);
        assertThat(response.getBody().getBrandId()).isEqualTo(1L);
        assertThat(response.getBody().getPriceList()).isEqualTo(1L);
        assertThat(response.getBody().getPrice().doubleValue()).isEqualTo(35.50);
        assertThat(response.getBody().getCurrency()).isEqualTo("EUR");
    }

    @Test
    public void testGetApplicablePriceV2_Case2() {
        // Test case 2: Request at 2020-06-14 16:00:00 for product 35455 and brand 1
        // Expected: Price List 2 (25.45 EUR)
        LocalDateTime dateTime = LocalDateTime.of(2020, 6, 14, 16, 0, 0);
        String url = buildUrl("v2", dateTime, 35455L, 1L);

        ResponseEntity<PriceResponse> response = restTemplate.getForEntity(url, PriceResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPriceList()).isEqualTo(2L);
        assertThat(response.getBody().getPrice().doubleValue()).isEqualTo(25.45);
    }

    @Test
    public void testGetApplicablePriceV2_Case3() {
        // Test case 3: Request at 2020-06-14 21:00:00 for product 35455 and brand 1
        // Expected: Price List 1 (35.50 EUR)
        LocalDateTime dateTime = LocalDateTime.of(2020, 6, 14, 21, 0, 0);
        String url = buildUrl("v2", dateTime, 35455L, 1L);

        ResponseEntity<PriceResponse> response = restTemplate.getForEntity(url, PriceResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPriceList()).isEqualTo(1L);
        assertThat(response.getBody().getPrice().doubleValue()).isEqualTo(35.50);
    }

    @Test
    public void testGetApplicablePriceV2_Case4() {
        // Test case 4: Request at 2020-06-15 10:00:00 for product 35455 and brand 1
        // Expected: Price List 3 (30.50 EUR)
        LocalDateTime dateTime = LocalDateTime.of(2020, 6, 15, 10, 0, 0);
        String url = buildUrl("v2", dateTime, 35455L, 1L);

        ResponseEntity<PriceResponse> response = restTemplate.getForEntity(url, PriceResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPriceList()).isEqualTo(3L);
        assertThat(response.getBody().getPrice().doubleValue()).isEqualTo(30.50);
    }

    @Test
    public void testGetApplicablePriceV2_Case5() {
        // Test case 5: Request at 2020-06-16 21:00:00 for product 35455 and brand 1
        // Expected: Price List 4 (38.95 EUR)
        LocalDateTime dateTime = LocalDateTime.of(2020, 6, 16, 21, 0, 0);
        String url = buildUrl("v2", dateTime, 35455L, 1L);

        ResponseEntity<PriceResponse> response = restTemplate.getForEntity(url, PriceResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPriceList()).isEqualTo(4L);
        assertThat(response.getBody().getPrice().doubleValue()).isEqualTo(38.95);
    }

    @Test
    public void testGetApplicablePriceV2_ProductNotFound() {
        // Test case 6: Request with a product that doesn't exist
        LocalDateTime dateTime = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        String url = buildUrl("v2", dateTime, 99999L, 1L);

        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(url, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("No applicable price found for product ID 99999");
    }

    @Test
    public void testGetApplicablePriceV2_BrandNotFound() {
        // Test case 7: Request with a brand that doesn't exist
        LocalDateTime dateTime = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        String url = buildUrl("v2", dateTime, 35455L, 99L);

        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(url, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("No applicable price found for product ID 35455 and brand ID 99");
    }

    @Test
    public void testGetApplicablePriceV2_DateOutOfRange() {
        // Test case 8: Request with a date outside of the range
        LocalDateTime dateTime = LocalDateTime.of(2019, 6, 14, 10, 0, 0);
        String url = buildUrl("v2", dateTime, 35455L, 1L);

        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(url, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("No applicable price found for product ID 35455 and brand ID 1");
    }

    @Test
    public void testGetApplicablePriceV1_InvalidDateTimeFormat() {
        // Test case: Request with an invalid date format
        String url = "http://localhost:" + port + "/api/prices/v1" +
                "?dateTime=2020-06-14" + // Missing time part
                "&productId=35455" +
                "&brandId=1";

        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(url, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Bad Request");
    }

    @Test
    public void testGetApplicablePriceV2_MissingParameter() {
        // Test case: Request missing required parameter
        String url = "http://localhost:" + port + "/api/prices/v2" +
                "?dateTime=2020-06-14T10:00:00" +
                // Missing productId
                "&brandId=1";

        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(url, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("Required parameter");
    }
}