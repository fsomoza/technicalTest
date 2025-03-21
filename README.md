# Price API Technical Test

## Overview
This is a Spring Boot application that serves as a dynamic pricing API, allowing clients to retrieve price information for products based on the application date, product ID, and brand ID. The API follows Hexagonal Architecture principles, providing a clean separation of concerns between the domain, application, and infrastructure layers.

## Features
- Query for applicable prices based on:
    - Date and time (when the price applies)
    - Product ID
    - Brand ID
- Automatic selection of the highest priority price when multiple options exist
- Comprehensive error handling
- Fully tested with unit and integration tests
- Database migrations managed through Flyway

## Technology Stack
- Java 17
- Spring Boot 3.4.3
- Spring Data JPA
- H2 Database (in-memory)
- Flyway for database migrations
- JUnit 5 for testing

## Architecture

The application follows the Hexagonal Architecture (aka Ports and Adapters) pattern:

### Domain Layer
- Contains the core business logic and domain entities
- Defines interfaces (ports) that the application expects to interact with

### Application Layer
- Contains the use case implementations
- Acts as a facade between the domain and infrastructure layers

### Infrastructure Layer
- Provides implementations for the interfaces defined in the domain layer
- Contains persistence adapters, API controllers, etc.

## Project Structure

```
src/main/java/com/example/test/
├── TestApplication.java
├── price/
    ├── application/
    │   └── services/
    │       └── PricePubicApiService.java
    ├── domain/
    │   ├── exceptions/
    │   │   └── PriceNotFoundException.java
    │   ├── models/
    │   │   └── Price.java
    │   └── ports/
    │       ├── in/
    │       │   └── PricePublicApiPort.java
    │       └── out/
    │           └── PriceRepositoryPort.java
    └── infrastructure/
        └── adapters/
            ├── httpapi/
            │   ├── GlobalExceptionHandler.java
            │   ├── PriceController.java
            │   └── dtos/
            │       ├── ErrorResponse.java
            │       └── PriceResponse.java
            └── repositories/
                ├── PriceRepositoryAdapter.java
                └── jparepository/
                    ├── JpaPriceRepository.java
                    └── entities/
                        └── JpaPriceEntity.java
```

## API Endpoints

### Get Applicable Price (Version 1)
```
GET /api/prices/v1?dateTime={dateTime}&productId={productId}&brandId={brandId}
```

### Get Applicable Price (Version 2)
```
GET /api/prices/v2?dateTime={dateTime}&productId={productId}&brandId={brandId}
```

#### Parameters
- `dateTime`: The date and time for which to find the applicable price (ISO-8601 format)
- `productId`: The ID of the product
- `brandId`: The ID of the brand/store

#### Response Example
```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 1,
  "startDate": "2020-06-14T00:00:00",
  "endDate": "2020-12-31T23:59:59",
  "price": 35.50,
  "currency": "EUR"
}
```

## Two Implementation Approaches

The service demonstrates two different approaches for finding applicable prices:

1. **V1 (Database-driven)**: Uses a query that directly retrieves the price with the highest priority.
2. **V2 (Application-driven)**: Retrieves all applicable prices and then uses Java streams to select the one with the highest priority.

Both endpoints are functionally equivalent but showcase different design philosophies.

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. Clone the repository
   ```
   git clone <repository-url>
   ```

2. Navigate to the project root
   ```
   cd test
   ```

3. Build the project
   ```
   ./mvnw clean package
   ```

4. Run the application
   ```
   ./mvnw spring-boot:run
   ```

5. The API will be available at: `http://localhost:8080`

### Testing

Run the tests with:
```
./mvnw test
```

## Database Schema

The application includes Flyway migrations that set up a `price` table with the following structure:

```sql
CREATE TABLE price (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_id BIGINT NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    price_list BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    priority INT NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL
);
```

The initial data includes price entries for testing different scenarios.

## H2 Console

When running in development mode, you can access the H2 database console at:

```
http://localhost:8080/h2-console
```

Connection details:
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: ` ` (empty)