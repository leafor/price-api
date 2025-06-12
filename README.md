# Price Service API

**Table of Contents**

* [Project Overview](#project-overview)
* [Architecture](#architecture)
* [Tech Stack](#tech-stack)
* [Getting Started](#getting-started)

    * [Prerequisites](#prerequisites)
    * [Installation](#installation)
    * [Configuration](#configuration)
* [Running the Application](#running-the-application)
* [API Endpoints](#api-endpoints)

    * [GET /api/prices](#get-apiprices)
* [Usage Examples](#usage-examples)
* [Testing](#testing)
* [Code Coverage](#code-coverage)
* [Packaging & Deployment](#packaging--deployment)
* [Contributing](#contributing)
* [Contact](#contact)

---

## Project Overview

The **Price Service API** is a Spring Boot application that provides real-time price lookup for products based on:

* **Product ID**
* **Brand ID**
* **Application Date**

It uses an in-memory H2 database to store price records with validity date ranges and prioritization logic.

## Features

* Query active price for a product at a specific date and brand
* Priority-based price resolution when overlapping ranges exist
* In-memory H2 initialization with sample data
* Robust validation and error handling
* OpenAPI documentation (Swagger UI)
* H2 Console access for ad-hoc queries

## Architecture

This project follows a **Hexagonal Architecture** (Ports & Adapters) pattern:

```
[Inbound Adapters: REST Controllers]
               ↓
          [Application]
      ┌─────────────────┐
      │   Domain Core   │
      │ (Entities & Ports) │
      └─────────────────┘
               ↓
[Outbound Adapters: JPA Repository]
```

* **Domain**: business entities and repository ports
* **Application**: services implementing use cases
* **Infrastructure**: adapters for REST, JPA, H2, OpenAPI

## Tech Stack

* Java 21
* Spring Boot 3.2.5
* Spring Web (REST)
* Spring Data JPA
* H2 Database (in-memory)
* MapStruct (object mapping)
* Springdoc OpenAPI (Swagger UI)
* JUnit 5, Mockito (unit testing)
* Spring Boot Test, TestRestTemplate (integration testing)
* JaCoCo (code coverage)

## Getting Started

### Prerequisites

* Java 21 SDK
* Maven 3.6+
* IntelliJ IDEA IDE

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/leafor/price-api
   cd price-service-api
   ```
2. Build the project:

   ```bash
   mvn clean install
   ```

### Configuration

The application uses default configurations in `src/main/resources/application.yml`. H2 console and OpenAPI UI are enabled by default:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:pricesdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: create
    defer-datasource-initialization: true
  sql:
    init:
      mode: always
  h2:
    console:
      enabled: true
      path: /h2-console
```

* **H2 Console:** `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:pricesdb`)
* **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`

## Running the Application

Start the service with Maven:

```bash
mvn spring-boot:run
```

Or run the generated JAR:

```bash
java -jar target/priceservice-0.0.1-SNAPSHOT.jar
```

## API Endpoints

### GET /api/prices

Retrieve the applicable price for a given product, brand, and application date.

**Request Parameters:**

| Parameter         | Type      | Description                                      |
| ----------------- | --------- | ------------------------------------------------ |
| `applicationDate` | `String`  | ISO-8601 date-time (e.g. `2020-06-14T16:00:00Z`) |
| `productId`       | `Long`    | Product identifier                               |
| `brandId`         | `Integer` | Brand (chain) identifier                         |

**Successful Response (200):**

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 4,
  "startDate": "2020-06-15T16:00:00Z",
  "endDate": "2020-12-31T23:59:59Z",
  "price": 38.95,
  "currency": "EUR"
}
```

**Error Responses:**

* `400 Bad Request` for invalid/missing params
* `404 Not Found` if no price found
* `405 Method Not Allowed` inval# Price Service API

**Table of Contents**

* [Project Overview](#project-overview)
* [Features](#features)
* [Architecture](#architecture)
* [Tech Stack](#tech-stack)
* [Getting Started](#getting-started)

    * [Prerequisites](#prerequisites)
    * [Installation](#installation)
    * [Configuration](#configuration)
* [Running the Application](#running-the-application)
* [API Endpoints](#api-endpoints)

    * [GET /api/prices](#get-apiprices)
* [Usage Examples](#usage-examples)
* [Testing](#testing)
* [Code Coverage](#code-coverage)
* [Packaging & Deployment](#packaging--deployment)
* [Contributing](#contributing)
* [License](#license)
* [Contact](#contact)

---

## Project Overview

The **Price Service API** is a Spring Boot application that provides real-time price lookup for products based on:

* **Product ID**
* **Brand ID**
* **Application Date**

It uses an in-memory H2 database to store price records with validity date ranges and prioritization logic.

## Features

* Query active price for a product at a specific date and brand
* Priority-based price resolution when overlapping ranges exist
* In-memory H2 initialization with sample data
* Robust validation and error handling
* OpenAPI documentation (Swagger UI)
* H2 Console access for ad-hoc queries

## Architecture

This project follows a **Hexagonal Architecture** (Ports & Adapters) pattern:

```
[Inbound Adapters: REST Controllers]
               ↓
          [Application]
      ┌─────────────────┐
      │   Domain Core   │
      │ (Entities & Ports) │
      └─────────────────┘
               ↓
[Outbound Adapters: JPA Repository]
```

* **Domain**: business entities and repository ports
* **Application**: services implementing use cases
* **Infrastructure**: adapters for REST, JPA, H2, OpenAPI

## Tech Stack

* Java 21
* Spring Boot 3.2.5
* Spring Web (REST)
* Spring Data JPA
* H2 Database (in-memory)
* MapStruct (object mapping)
* Springdoc OpenAPI (Swagger UI)
* JUnit 5, Mockito (unit testing)
* Spring Boot Test, TestRestTemplate (integration testing)
* JaCoCo (code coverage)

## Getting Started

### Prerequisites

* Java 21 SDK
* Maven 3.6+
* IntelliJ IDEA IDE

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/leafor/price-service-api.git
   cd price-service-api
   ```
2. Build the project:

   ```bash
   mvn clean install
   ```

### Configuration

The application uses default configurations in `src/main/resources/application.yml`. H2 console and OpenAPI UI are enabled by default:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:pricesdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: create
    defer-datasource-initialization: true
  sql:
    init:
      mode: always
  h2:
    console:
      enabled: true
      path: /h2-console
```

* **H2 Console:** `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:pricesdb`)
* **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`

## Running the Application

Start the service with Maven:

```bash
mvn spring-boot:run
```

Or run the generated JAR:

```bash
java -jar target/priceservice-0.0.1-SNAPSHOT.jar
```

## API Endpoints

### GET /api/prices

Retrieve the applicable price for a given product, brand, and application date.

**Request Parameters:**

| Parameter         | Type      | Description                                      |
| ----------------- | --------- | ------------------------------------------------ |
| `applicationDate` | `String`  | ISO-8601 date-time (e.g. `2020-06-14T16:00:00Z`) |
| `productId`       | `Long`    | Product identifier                               |
| `brandId`         | `Integer` | Brand (chain) identifier                         |

**Successful Response (200):**

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 4,
  "startDate": "2020-06-15T16:00:00Z",
  "endDate": "2020-12-31T23:59:59Z",
  "price": 38.95,
  "currency": "EUR"
}
```

**Error Responses:**

* `400 Bad Request` for invalid/missing params
* `404 Not Found` if no price found
* `405 Method Not Allowed` invalid url api

## Usage Examples

```bash
# Example: query price at 16:00 on 2020-06-14
curl -X GET \
  "http://localhost:8080/api/prices?applicationDate=2020-06-14T16:00:00Z&productId=35455&brandId=1" \
  -H "Accept: application/json"
```

## Testing

Run all tests (unit + integration):

```bash
mvn test
```

* **Unit tests:** located under `src/test/java/com/example/priceapi/application/service`
* **Controller tests:** under `infrastructure/adapter/in`
* **Integration tests:** under `integration` package

## Code Coverage

The project is configured with JaCoCo. Generate the coverage report:

```bash
mvn clean verify
```

Open `target/site/jacoco/index.html` to view detailed coverage. DTOs, models, requests, and Lombok-generated code are excluded.

## Packaging & Deployment

Build a production JAR:

```bash
mvn clean package -DskipTests
```

Deploy the JAR to your environment, or containerize using Docker:

```dockerfile
FROM eclipse-temurin:17-jre
COPY target/priceservice-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -am 'Add feature'`)
4. Push to branch (`git push origin feature/YourFeature`)
5. Open a Pull Request

Please follow the existing code style and include tests for new functionality.

## Contact

For questions or suggestions, please open an issue or contact the maintainer:

* **Name:** Leandro Fortete
* **Email:** [leandro@example.com](mailto:leandro@example.com)
* **GitHub:** [github.com/leandrofortete](https://github.com/leandrofortete)
  ud 

## Usage Examples

```bash
# Example: query price at 16:00 on 2020-06-14
curl -X GET \
  "http://localhost:8080/api/prices?applicationDate=2020-06-14T16:00:00Z&productId=35455&brandId=1" \
  -H "Accept: application/json"
```

## Testing

Run all tests (unit + integration):

```bash
mvn test
```

* **Unit tests:** located under `src/test/java/com/example/priceapi/application/service`
* **Controller tests:** under `infrastructure/adapter/in`
* **Integration tests:** under `integration` package

## Code Coverage

The project is configured with JaCoCo. Generate the coverage report:

```bash
mvn clean verify
```

Open `target/site/jacoco/index.html` to view detailed coverage. DTOs, models, requests, and Lombok-generated code are excluded.

## Packaging & Deployment

Build a production JAR:

```bash
mvn clean package -DskipTests
```

Deploy the JAR to your environment, or containerize using Docker:

```dockerfile
FROM eclipse-temurin:17-jre
COPY target/priceservice-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -am 'Add feature'`)
4. Push to branch (`git push origin feature/YourFeature`)
5. Open a Pull Request

Please follow the existing code style and include tests for new functionality.

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

## Contact

For questions or suggestions, please open an issue or contact the maintainer:

* **Name:** Leandro Fortete
* **Email:** [leandrofortete@gmail.com](mailto:leandrofortete@gmail.com)
* **GitHub:** [github.com/leandrofortete](https://github.com/leafor)
