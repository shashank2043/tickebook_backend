# Tickebook Backend

Ticket booking platform built with **Spring Boot 3.5**, **Spring Cloud**, and **Java 25** using a microservices architecture.

## Architecture

```
┌─────────────────┐
│   API Gateway   │ :8080  (Spring Cloud Gateway + JWT Auth)
└────────┬────────┘
         │
    ┌────┴────────────────────────────────────────────────────┐
    │                                                         │
┌───▼──────┐   ┌────────────┐   ┌────────────┐  ┌──────────┐  ┌─────────┐  ┌──────────┐
│  Auth    │   │  Admin     │   │  Consumer  │  │  Movie   │  │ Payment │  │  Show    │
│ :8081    │   │ :8082      │   │ :8083      │  │ :8084    │  │ :8085   │  │ :8086    │
└──────────┘   └────────────┘   └────────────┘  └──────────┘  └─────────┘  └──────────┘
                                                                 │
                    ┌────────────────┐  ┌────────────┐          │  ┌──────────┐  ┌──────────┐
                    │ Mail Server    │  │ Monitoring │          └──► Theatre  │  │  etc.    │
                    │ :8090          │  │ :9090      │             │ :8087    │  │          │
                    └────────────────┘  └────────────┘             └──────────┘  └──────────┘
```

## Services

| Service | Port | Description |
|---|---|---|
| **api-gateway** | 8080 | Central routing, JWT auth, Rate limiting, Swagger aggregation |
| **auth-service** | 8081 | Registration, login, OTP verification, JWT issuance |
| **admin-service** | 8082 | Role approval, theatre change requests, theatre approval |
| **consumer-service** | 8083 | Booking flow, seat reservation, ticketing |
| **movie-service** | 8084 | Movie catalog management |
| **payment-service** | 8085 | Payment processing (mock gateway) |
| **show-service** | 8086 | Show scheduling, seat mapping |
| **theatre-service** | 8087 | Theatre, screen, and seat management |
| **mail-server** | 8090 | Email notifications via Kafka |
| **discovery-server** | 8761 | Netflix Eureka service registry |
| **config-server** | 8888 | Centralized configuration (Spring Cloud Config) |
| **monitoring-service** | 9090 | Spring Boot Admin dashboard |

## Infrastructure

| Component | Port | Purpose |
|---|---|---|
| Kafka | 9092 | Async messaging (e.g., OTP emails) |
| MySQL | 3306 | Primary database |
| Zipkin | 9411 | Distributed tracing |

## Quick Start

### All-in-one with Docker Compose

```bash
# Build all images then start the stack
mvn clean package -DskipTests -B
docker-compose up -d

# View logs of a specific service
docker-compose logs -f api-gateway

# Tear down
docker-compose down
```

### Development (without Docker)

1. Build the shared DTOs first:

   ```bash
   mvn -pl common-dtos install -DskipTests
   ```

2. Start infrastructure (MySQL & Kafka):

   ```bash
   docker-compose up -d mysql kafka config-server discovery-server
   ```

3. Run individual services from your IDE or CLI:

   ```bash
   cd auth-service && mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

## Building

```bash
# Build everything
mvn clean package -DskipTests

# Build a single service
mvn -pl auth-service -am clean package -DskipTests

# Install common-dtos locally (required before building other services)
mvn -pl common-dtos install -DskipTests
```

## Testing

```bash
# Run all tests
mvn test

# Test a single service
mvn -pl auth-service test

# Run a specific test class
mvn -pl auth-service test -Dtest=AuthControllerTest

# Run a single test method
mvn -pl auth-service test -Dtest=AuthControllerTest#testLoginSuccess
```

## Technology Stack

- **Java 25** with **Spring Boot 3.5** and **Spring Cloud 2025.1**
- **Spring Cloud Gateway** (WebFlux) for reverse proxy and routing
- **Spring Security** for JWT-based authentication
- **Netflix Eureka** for service discovery
- **Spring Cloud Config** for centralized configuration
- **MySQL 8** for persistence
- **Apache Kafka** for event-driven messaging
- **Spring Boot Admin** for monitoring
- **Zipkin** + **Micrometer Tracing** for distributed tracing
- **Resilience4j** for circuit breaker patterns
- **Swagger/OpenAPI** via springdoc for API docs

## Configuration

Services use Spring Cloud Config with two profiles:

- **dev** — reads config from the local `config-repo/` submodule
- **docker** — reads from the mounted config-server in Docker

The config files are stored in the `config-repo/` submodule, which points to: [github.com/shashank2043/tickebook_config](https://github.com/shashank2043/tickebook_config)

## API Routes (via Gateway :8080)

| Prefix | Routed To |
|---|---|
| `/api/movies/**` | movie-service |
| `/api/bookings/**`, `/api/tickets/**`, `/api/booking-seats/**` | consumer-service |
| `/api/shows/**` | show-service |
| `/api/screens/**`, `/api/seats/**`, `/api/theatres/**`, `/api/owner/**` | theatre-service |
| `/api/theatre-approval-requests/**`, `/api/role-approval-requests/**` | admin-service |
| `/auth/**`, `/api/user/**` | auth-service |
| `/payments/**` | payment-service |
| `/metrics/**` | monitoring-service |

### Swagger UI

Aggregated at `http://localhost:8080/swagger-ui.html` after startup.

## Kubernetes

Raw K8s manifests are provided in the `kubernetes/` directory:

```bash
kubectl apply -f kubernetes/
```

## Project Structure

```
tickebook_backend/
├── api-gateway/          # Gateway, auth filter, Swagger aggregation
├── auth-service/         # Authentication & OTP
├── admin-service/        # Admin approvals & reviews
├── consumer-service/     # Booking, tickets, browsing
├── movie-service/        # Movie catalog
├── payment-service/      # Payment processing
├── show-service/         # Show scheduling
├── theatre-service/      # Theatre, screens, seats
├── mail-server/          # Kafka-driven email consumer
├── config-server/        # Spring Cloud Config Server
├── discovery-server/     # Eureka registry
├── monitoring-service/   # Spring Boot Admin
├── common-dtos/          # Shared DTO library
├── config-repo/          # Config files (git submodule)
├── docker/               # MySQL init scripts
├── kubernetes/           # K8s deployment manifests
└── docker-compose.yml    # Full stack
```