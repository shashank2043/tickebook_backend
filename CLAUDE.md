# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Tickebook** — a movie ticket booking backend built as a Java 25 / Spring Boot 3.5 microservices architecture with Spring Cloud.

## Architecture

12 Maven modules forming a microservices ecosystem:

### Infrastructure
- **config-server** — Spring Cloud Config Server (reads from `config-repo/` locally, git otherwise)
- **discovery-server** — Netflix Eureka service registry (port 8761)
- **api-gateway** — Spring Cloud Gateway (WebFlux, port 8080) with JWT auth via `SecurityConfig`/`JwtWebFilter`, Zipkin tracing, Resilience4j circuit breaker, aggregated Swagger UI
- **monitoring-service** — Spring Boot Admin dashboard (port 9090)
- **common-dtos** — Shared DTOs library consumed by other services

### Business Services
- **auth-service** (8081) — Registration, login, OTP verification, JWT issuance
- **admin-service** (8082) — Role approvals, theatre change requests, admin profile
- **consumer-service** (8083) — Booking, ticketing, seat reservation, browsing
- **movie-service** (8084) — Movie catalog
- **payment-service** (8085) — Payment processing (mock gateway)
- **show-service** (8086) — Show management with Feign client to theatre-service
- **theatre-service** (8087) — Theatres, screens, seats, owner profiles
- **mail-server** (8090) — Email via Kafka consumption

### Cross-cutting concerns
- **Kafka** for async messaging (e.g., auth → mail for OTP emails)
- **Feign clients** for synchronous inter-service communication
- **Zipkin** for distributed tracing (configured in gateway config)
- **MySQL** database (shared across services, each service has its own schema)

### Service Discovery
All business services register with Eureka. The API Gateway routes via `lb://` discovery. Routes are defined in `config-repo/api-gateway-docker.yml`.

## Key Commands

```bash
# Build entire project
mvn clean package -DskipTests

# Build a single module (plus dependencies)
mvn -pl auth-service -am clean package -DskipTests

# Run all tests
mvn test

# Run tests for a single module
mvn -pl auth-service test

# Run a single test class
mvn -pl auth-service test -Dtest=AuthControllerTest

# Run a single test method
mvn -pl auth-service test -Dtest=AuthControllerTest#testName

# Start all infrastructure with Docker
docker-compose up -d

# Start specific service
docker-compose up -d kafka mysql config-server discovery-server
```

## Service Profiles

Each service supports two Spring profiles:
- **docker** — reads config from config-server over HTTP; connects to Docker Compose infrastructure
- **dev** — reads config from `config-repo/` local directory; typically connects to localhost MySQL

## Configuration

- **config-repo/** — local configuration files for Spring Cloud Config Server (per-service: `{service}-{profile}.yml`)
- **docker-compose.yml** — full stack: Kafka, MySQL, Zipkin, and all services
- **kubernetes/** — raw K8s manifests for each service

## Inter-service Communication Patterns

- **Synchronous**: Feign HTTP clients (e.g., `TheatreClient` in show-service, `AdminClient` in auth-service). Fallbacks exist via `circuitbreaker-reactor-resilience4j` in the gateway.
- **Asynchronous**: Kafka producer/consumer (e.g., auth-service publishes events, mail-server consumes via `MailKafkaConsumer`)

## Important Notes

- The `common-dtos` module must be installed to local Maven (`mvn install`) before other services can build against it
- Each microservice follows the standard Spring Boot layered structure: controller → service → repository → model
- The API Gateway handles JWT validation via Spring Security (WebFlux-based `JwtWebFilter`), not the commented-out `AuthenticationFilter`
- Database init script at `docker/mysql/init.sql` creates the initial databases