# domain-customer-kyc-kyb

Domain layer microservice for Know Your Customer (KYC) and Know Your Business (KYB) verification processes. This service orchestrates identity verification workflows for both individual customers and business entities within the Firefly platform.

Repository: [https://github.com/firefly-oss/domain-customer-kyc-kyb](https://github.com/firefly-oss/domain-customer-kyc-kyb)

---

## Overview

`domain-customer-kyc-kyb` provides a reactive REST API for managing compliance-driven verification lifecycles. It supports the full case-based workflow: opening a case, attaching documentary evidence, marking dossiers complete, recording PEP/adverse-media findings (KYC), verifying or rejecting cases, and handling expiration and renewal.

### Key Features

- **KYC case management** -- open cases, attach identity/POA/selfie documents, record PEP and adverse media findings, verify or reject, expire, and renew verifications.
- **KYB case management** -- start business verification processes, attach statutes/registries/shareholder records, validate UBOs, verify or reject entities, expire, and renew.
- **Saga-orchestrated transactions** -- every operation returns a `SagaResult` via the FireflyFramework Transactional Saga Engine, ensuring distributed data consistency with compensating actions.
- **Reactive, non-blocking architecture** -- built on Spring WebFlux and Project Reactor for high-throughput, low-latency processing.
- **OpenAPI documentation** -- integrated Swagger UI via SpringDoc for interactive API exploration.
- **Observability** -- Prometheus metrics via Micrometer and Spring Boot Actuator health endpoints.

---

## Architecture

### Module Structure

| Module | Purpose |
|--------|---------|
| `domain-customer-kyc-kyb-core` | Business logic: service interfaces/implementations, commands, and domain models for KYC and KYB. |
| `domain-customer-kyc-kyb-interfaces` | Interface adapters: bridges between web layer and core domain; depends on core. |
| `domain-customer-kyc-kyb-infra` | Infrastructure: configuration properties, validators, external client setup. |
| `domain-customer-kyc-kyb-web` | Deployable Spring Boot application: REST controllers, OpenAPI config, actuator endpoints. |
| `domain-customer-kyc-kyb-sdk` | Client SDK placeholder for consumers of this service's API. |

### Dependency Flow

```
web --> interfaces --> core --> infra
```

### Technology Stack

| Technology | Purpose |
|------------|---------|
| Java 25 | Language runtime |
| Spring Boot (WebFlux) | Reactive web framework |
| Project Reactor | Reactive streams |
| [FireflyFramework BOM 26.01.01](https://github.com/fireflyframework/) | Parent POM, dependency management, shared libraries |
| [FireflyFramework Transactional Saga Engine](https://github.com/fireflyframework/) | Distributed saga orchestration (`SagaResult`, `@EnableTransactionalEngine`) |
| [FireflyFramework Utils](https://github.com/fireflyframework/) | Common utilities |
| [FireflyFramework Domain](https://github.com/fireflyframework/) | Domain abstractions |
| [FireflyFramework Validators](https://github.com/fireflyframework/) | Validation utilities |
| [FireflyFramework Web](https://github.com/fireflyframework/) | Common web configurations |
| common-platform-customer-mgmt-sdk | SDK client for the Customer Management core platform |
| SpringDoc OpenAPI (WebFlux UI) | API documentation |
| Micrometer + Prometheus | Metrics and monitoring |
| MapStruct | Object mapping |
| Lombok | Boilerplate reduction |

---

## Setup

### Prerequisites

- **Java 25** (or later)
- **Apache Maven 3.9+**
- Access to the FireflyFramework Maven repository for `org.fireflyframework` dependencies
- Access to the `common-platform-customer-mgmt-sdk` artifact

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `SERVER_ADDRESS` | `localhost` | Address the server binds to |
| `SERVER_PORT` | `8080` | HTTP port |

> **Note:** This project currently does not include an `application.yaml` resource file. Configuration is expected to be supplied externally or via the FireflyFramework parent defaults. See the `CustomerDomainKycKybApplication` class for OpenAPI metadata (server URLs: `http://core.getfirefly.io/customer-domain-kyckyb` for dev, `/` for local).

### Build

```bash
# Full build
mvn clean install

# Skip tests
mvn clean install -DskipTests
```

### Run

```bash
# Run via Spring Boot Maven plugin
mvn -pl domain-customer-kyc-kyb-web spring-boot:run

# Or run the packaged JAR
java -jar domain-customer-kyc-kyb-web/target/domain-customer-kyc-kyb.jar
```

---

## API Endpoints

All endpoints return reactive `Mono<ResponseEntity<Object>>` responses. The base API path prefix is versioned.

### KYC Endpoints (`/api/v1/kyc`)

| Method | Path | Summary | Description |
|--------|------|---------|-------------|
| `POST` | `/api/v1/kyc/cases` | Start KYC | Open a KYC case with required due diligence level |
| `POST` | `/api/v1/kyc/cases/{caseId}/evidence` | Attach Evidence | Attach identity/POA/selfie documents (fingerprint ref) |
| `POST` | `/api/v1/kyc/cases/{caseId}/docs-complete` | Mark Documents Complete | Mark dossier complete for review |
| `POST` | `/api/v1/kyc/cases/{caseId}/pep-adverse-media` | Record PEP/Adverse Media | Record PEP/adverse media status affecting decision |
| `POST` | `/api/v1/kyc/cases/{caseId}/verify` | Verify KYC | Approve KYC at level and set expiration |
| `POST` | `/api/v1/kyc/cases/{caseId}/fail` | Fail KYC | Close as rejected (fraud/inconsistency) |
| `POST` | `/api/v1/kyc/cases/{caseId}/expire` | Expire KYC | Mark verification expired by time |
| `POST` | `/api/v1/kyc/cases/{caseId}/renew` | Renew KYC | Renew verification with new level/expiry |

### KYB Endpoints (`/api/v1/kyb`)

| Method | Path | Summary | Description |
|--------|------|---------|-------------|
| `POST` | `/api/v1/kyb/cases` | Start KYB | Open a KYB process for an organization |
| `POST` | `/api/v1/kyb/cases/{caseId}/evidence` | Attach Evidence | Attach statutes, registries, shareholder records, POA |
| `POST` | `/api/v1/kyb/cases/{caseId}/docs-complete` | Mark Documents Complete | Mark KYB dossier complete |
| `POST` | `/api/v1/kyb/cases/{caseId}/verify` | Verify KYB | Verify entity (requires UBOs validated) and set expiry |
| `POST` | `/api/v1/kyb/cases/{caseId}/fail` | Fail KYB | Reject KYB (opaque structure/sanctions/insufficient docs) |
| `POST` | `/api/v1/kyb/cases/{caseId}/expire` | Expire KYB | Mark KYB expired by time |
| `POST` | `/api/v1/kyb/cases/{caseId}/renew` | Renew KYB | Renew KYB level/expiry |

### Evidence Command Payload

Both KYC and KYB evidence endpoints accept the same structure:

```json
{
  "caseId": "uuid",
  "documentType": "string",
  "documentName": "string",
  "documentContent": "string",
  "mimeType": "string",
  "documentSize": 0,
  "fingerprint": "string",
  "description": "string"
}
```

---

## Development Guidelines

### Project Conventions

- **Reactive-only**: all service methods return `Mono` or `Flux`. Do not use blocking calls.
- **Saga pattern**: business operations use the FireflyFramework `SagaEngine` with `SagaResult` return types and compensating actions.
- **Command pattern**: each operation is modeled as a command object (e.g., `AttachEvidenceCommand`).
- **Layer separation**: controllers delegate to service interfaces; implementations live in `core`.
- **Lombok + MapStruct**: use `@Data`, `@RequiredArgsConstructor` for models; MapStruct for DTO mapping.

### Package Structure

```
com.firefly.domain.kyc.kyb
  +-- core
  |     +-- kyc
  |     |     +-- commands/          # KYC command objects
  |     |     +-- services/          # KYC service interface
  |     |     +-- services/impl/     # KYC service implementation
  |     +-- kyb
  |           +-- commands/          # KYB command objects
  |           +-- services/          # KYB service interface
  |           +-- services/impl/     # KYB service implementation
  +-- web
        +-- controller/              # REST controllers (KycController, KybController)
        +-- CustomerDomainKycKybApplication.java
```

### Running Tests

```bash
mvn test
```

Dependencies for testing include `spring-boot-starter-test` and `reactor-test`.

---

## Monitoring

### Actuator Endpoints

The web module includes `spring-boot-starter-actuator` with the following expected endpoints:

| Endpoint | Purpose |
|----------|---------|
| `/actuator/health` | Application health check with liveness/readiness probes |
| `/actuator/info` | Build information |
| `/actuator/prometheus` | Prometheus-compatible metrics |

### Metrics

Prometheus metrics are exported via `micrometer-registry-prometheus`. Scrape the `/actuator/prometheus` endpoint for time-series data.

### API Documentation

Interactive API documentation is available at:

- **Swagger UI**: `/swagger-ui.html`
- **OpenAPI JSON**: `/v3/api-docs`
