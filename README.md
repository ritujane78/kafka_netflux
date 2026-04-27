# Netflux (Kafka + WebFlux Microservices)

## Overview

Netflux is a multi-module, event-driven microservices system built using
**Spring Boot, WebFlux, and Apache Kafka**.

It simulates a real-time movie recommendation platform where: - Movies
are added → events published - Customer preferences change → events
published - Recommendation service consumes events → generates
recommendations

------------------------------------------------------------------------

## Architecture

### Services

-   **movie-service**
    -   Manages movie catalog
    -   Publishes `MovieAddedEvent`
-   **customer-service**
    -   Manages users and genre preferences
    -   Publishes `CustomerGenreUpdatedEvent`
-   **recommendation-service**
    -   Consumes Kafka events
    -   Maintains state
    -   Generates recommendations via REST API
-   **netflux-events**
    -   Shared event models across services

------------------------------------------------------------------------

## Event Flow

1.  Movie added → Kafka (`MovieAddedEvent`)
2.  User updates genre → Kafka (`CustomerGenreUpdatedEvent`)
3.  Recommendation service consumes both
4.  Matches movies with user preferences
5.  Exposes recommendations via API

------------------------------------------------------------------------

##  Tech Stack

-   Java + Spring Boot
-   Spring WebFlux (Reactive)
-   Apache Kafka
-   Testcontainers (Kafka integration tests)
-   Maven (multi-module)

------------------------------------------------------------------------

## Project Structure

    kafka_netflux/
     ├── movie-service/
     ├── customer-service/
     ├── recommendation-service/
     ├── netflux-events/
     ├── docker-compose.yml
     └── pom.xml

------------------------------------------------------------------------

## Running the Project

### 1. Start Kafka (Required)

Make sure Kafka broker is running before starting services.

Use the provided Docker setup:

    docker-compose up -d

------------------------------------------------------------------------

### 2. Build the Project

    mvn clean install

------------------------------------------------------------------------

### 3. Start Services (Important Order)

Start services in the following order:

1.  **movie-service** (port:7070)
2.  **customer-service** (port:6060)
3.  **recommendation-service** (port:8080)

```{=html}
<!-- -->
```
    cd movie-service
    mvn spring-boot:run

    cd ../customer-service
    mvn spring-boot:run

    cd ../recommendation-service
    mvn spring-boot:run

> Recommendation service depends on events produced by the other
> services, so it should be started last.

------------------------------------------------------------------------

##  Static UI

A **static HTML file** is provided in the project.

-   Can be used to test the recommendation flow
-   Interacts with backend APIs
-   Useful for quick manual testing without frontend setup

    Access it at http://localhost:8080/ (After running all the services.)

------------------------------------------------------------------------

##  Key Kafka Events

### MovieAddedEvent

    {
      "movieId": "...",
      "title": "...",
      "genre": "..."
    }

### CustomerGenreUpdatedEvent

    {
      "customerId": "...",
      "genres": ["Action", "Sci-Fi"]
    }

------------------------------------------------------------------------

##  Key Highlights

-   Event-driven microservices architecture
-   Reactive non-blocking APIs (WebFlux)
-   Kafka-based asynchronous communication
-   Decoupled services via event streaming
-   Integration tested using Testcontainers

------------------------------------------------------------------------

##  Testing

The project includes **integration testing strategies for event-driven systems**:

### 1. Test Binders (Lightweight Testing)
- Uses Spring Cloud Stream test binders
- Simulates Kafka without requiring a real broker
- Fast and ideal for unit-level integration testing of producers/consumers

### 2. Kafka Integration Tests (Real Broker)
- Uses **Testcontainers**
- Spins up a real Kafka broker in Docker during tests
- Validates end-to-end event flow across services