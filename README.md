# Hotel Property View

REST API for storing and searching hotel information.

I built this project as a small backend exercise focused on Spring Boot, JPA queries, validation and testing. Compared with my older projects, this one is intentionally simple and structured around a conventional service/repository architecture.

## Features

- create hotels;
- get all hotels or a single hotel by id;
- search by name, brand, city, country and amenity;
- add amenities to an existing hotel;
- build simple histograms by brand, city, country or amenities;
- request validation and centralized exception handling;
- OpenAPI/Swagger documentation.

Dynamic search is implemented with Spring Data JPA Specifications.

## Stack

Java 21, Spring Boot, Spring Data JPA, Hibernate, PostgreSQL/MySQL/H2, Liquibase, MapStruct, Bean Validation, OpenAPI, Maven and Docker.

## Tests

The project includes both unit and integration tests for the service, mapper, specifications, repository and controller layers.

Tests use Spring Boot Test, JUnit and Mockito.

## Running the application

By default the application starts with the H2 profile:

```bash
./mvnw spring-boot:run
```

The API runs on:

```
http://localhost:8092
```

Swagger UI:

```
http://localhost:8092/swagger-ui.html
```

Database-specific profiles are also available for PostgreSQL and MySQL in `src/main/resources`.

## API examples

```
GET  /property-view/hotels
GET  /property-view/hotels/{id}
GET  /property-view/search?city=Minsk
POST /property-view/hotels
POST /property-view/hotels/{id}/amenities
GET  /property-view/histogram/{param}
```

This repository is mainly a backend code sample rather than a finished end-user product.
