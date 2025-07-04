# Inventory Service - Clean Architecture Implementation

A Spring Boot microservice for inventory management built following Clean Architecture principles, implementing the Ports & Adapters pattern with proper dependency inversion.

## 📋 Table of Contents

- [Architecture Overview](#architecture-overview)
- [Project Structure](#project-structure)
- [Clean Architecture Layers](#clean-architecture-layers)
- [Key Features](#key-features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Testing Strategy](#testing-strategy)
- [Exception Handling](#exception-handling)
- [Database Schema](#database-schema)

## 🏗️ Architecture Overview

This project implements Clean Architecture with the following core principles:

- **Dependency Inversion**: Inner layers define interfaces, outer layers implement them
- **Separation of Concerns**: Each layer has a single responsibility
- **Framework Independence**: Business logic is isolated from frameworks
- **Testability**: Easy to unit test with proper mocking

### Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                        Controllers                          │
│              (HTTP/REST API Interface)                      │
└─────────────────────┬───────────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────────┐
│                   Application                               │
│           (Use Cases & Business Rules)                      │
└─────────────────────┬───────────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────────┐
│                     Domain                                  │
│              (Entities & Business Logic)                    │
└─────────────────────┬───────────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────────┐
│                 Infrastructure                              │
│        (Database, External APIs, Messaging)                │
└─────────────────────────────────────────────────────────────┘
```

## 📁 Project Structure

```
src/main/kotlin/com/loc/inventory_service/
├── domain/                          # Enterprise Business Rules
│   └── model/
│       └── Inventory.kt            # Domain Entity
│
├── application/                     # Application Business Rules
│   ├── service/
│   │   ├── AddInventoryService.kt  # Use Case Implementation
│   │   └── CheckStockService.kt    # Use Case Implementation
│   └── port/
│       └── InventoryRepositoryPort.kt # Repository Contract
│
├── infrastructure/                  # Frameworks & Drivers
│   ├── adapter/
│   │   └── InventoryRepositoryAdapter.kt # Repository Implementation
│   ├── entity/
│   │   └── InventoryEntity.kt      # JPA Entity
│   ├── repository/
│   │   └── JpaInventoryRepository.kt # JPA Repository Interface
│   └── mapper/
│       └── InventoryEntityMapper.kt # Domain ↔ Entity Mapping
│
├── controller/                      # Interface Adapters
│   ├── InventoryController.kt      # REST Controller
│   └── mapper/
│       ├── AddInventoryMapper.kt   # DTO ↔ Domain Mapping
│       └── StockCheckMapper.kt     # DTO ↔ Domain Mapping
│
└── exception/                       # Exception Handling
    ├── handler/
    │   └── GlobalExceptionHandler.kt # Global Exception Handler
    └── infrastructure/
        └── InfrastructureException.kt # Infrastructure Exceptions
```

## 🎯 Clean Architecture Layers

### 1. Domain Layer (Innermost)
- **Purpose**: Contains enterprise business rules and entities
- **Dependencies**: None (pure business logic)
- **Components**:
  - `Inventory`: Core domain entity representing inventory items with SKU code and quantity

### 2. Application Layer
- **Purpose**: Contains application-specific business rules (use cases)
- **Dependencies**: Only depends on Domain layer
- **Components**:
  - `AddInventoryService`: Handles inventory addition and updates
  - `CheckStockService`: Validates stock availability
  - `InventoryRepositoryPort`: Port (interface) for data access

### 3. Infrastructure Layer (Outermost)
- **Purpose**: Contains frameworks, databases, external services
- **Dependencies**: Implements interfaces from inner layers
- **Components**:
  - `InventoryRepositoryAdapter`: Implements domain repository contract
  - `InventoryEntity`: JPA entity for database persistence
  - `JpaInventoryRepository`: Spring Data JPA repository
  - `InventoryEntityMapper`: Maps between domain and JPA entities

### 4. Controller Layer (Interface Adapters)
- **Purpose**: Handles HTTP requests and responses
- **Dependencies**: Uses Application layer services
- **Components**:
  - `InventoryController`: REST API endpoints
  - `AddInventoryMapper`: Maps between DTOs and domain models
  - `StockCheckMapper`: Maps between DTOs and domain models

## ✨ Key Features

- **Clean Architecture Implementation**: Proper dependency inversion and layer separation
- **Inventory Management**: Add new inventory items or update existing quantities
- **Stock Checking**: Validate product availability for given quantities
- **Comprehensive Exception Handling**: Layered exception handling with global handler
- **Database Integration**: JPA/Hibernate with MySQL and Liquibase migrations
- **API Documentation**: OpenAPI 3.0 specification with Swagger UI
- **Comprehensive Testing**: Unit and integration tests for all layers

## 🛠️ Technologies Used

- **Framework**: Spring Boot 3.x
- **Language**: Kotlin
- **Database**: MySQL 8.3 with JPA/Hibernate
- **Migration**: Liquibase
- **Documentation**: OpenAPI 3.0 (Swagger)
- **Testing**: JUnit 5, MockK, SpringMockK
- **Build Tool**: Gradle with Kotlin DSL
- **Code Generation**: OpenAPI Generator

## 🚀 Getting Started

### Prerequisites

- JDK 17 or higher
- Docker and Docker Compose
- Gradle 7.x or higher

### Running the Application

1. **Start Infrastructure Services**:
   ```bash
   cd server
   docker-compose up -d
   ```

2. **Run the Application**:
   ```bash
   ./gradlew bootRun
   ```

3. **Access the API**:
   - Application: http://localhost:8082
   - Swagger UI: http://localhost:8082/swagger-ui.html
   - API Docs: http://localhost:8082/v3/api-docs

### Configuration

The application uses `application.yml` for configuration:

```yaml
server:
  port: 8082

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/inventory_service
    username: root
    password: mysql
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
    
  liquibase:
    change-log: classpath:db/changelog/db.changelog-master.xml
```

## 📚 API Documentation

### Inventory Management Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/inventory/check-stock` | Check if product is in stock |
| POST | `/api/inventory/add` | Add new inventory or update existing |

### Request/Response Examples

**Check Stock**:
```bash
GET /api/inventory/check-stock?skuCode=IPHONE13-128&quantity=2
```

**Response**:
```json
true
```

**Add Inventory**:
```json
POST /api/inventory/add
{
  "skuCode": "IPHONE13-128",
  "quantity": 10
}
```

**Response**:
```json
{
  "skuCode": "IPHONE13-128",
  "quantity": 10
}
```

## 🧪 Testing Strategy

### Test Structure Following Clean Architecture

```
src/test/kotlin/com/loc/inventory_service/
├── application/
│   ├── AddInventoryServiceTest.kt        # Use Case Tests
│   └── CheckStockServiceTest.kt          # Use Case Tests
├── controller/
│   ├── InventoryControllerTest.kt        # API Integration Tests
│   └── mapper/
│       ├── AddInventoryMapperTest.kt     # DTO Mapping Tests
│       └── StockCheckMapperTest.kt       # DTO Mapping Tests
└── infrastructure/
    ├── adapter/
    │   └── InventoryRepositoryAdapterTest.kt # Repository Tests
    └── mapper/
        └── InventoryEntityMapperTest.kt      # Entity Mapping Tests
```

### Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test categories
./gradlew test --tests "*Controller*"
./gradlew test --tests "*Service*"
./gradlew test --tests "*Repository*"

# Run tests with coverage
./gradlew test jacocoTestReport
```

## 🚨 Exception Handling

The application implements a layered exception handling strategy:

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Controller    │    │   Service Layer  │    │   Repository    │
│                 │    │                  │    │                 │
│ HTTP Requests   │───▶│ Business Logic   │───▶│ Database Access │
└─────────────────┘    └──────────────────┘    └─────────────────┘
         │                       │                      │
         │              ┌────────▼────────┐             │
         │              │ Domain          │             │
         │              │ Exceptions      │             │
         │              │ • Business      │             │
         │              │ • Validation    │             │
         │              └─────────────────┘             │
         │                                              │
         │              ┌─────────────────┐             │
         │              │ Infrastructure  │◀────────────┘
         │              │ Exceptions      │
         │              │ • Database      │
         │              │ • Connection    │
         │              │ • SQL Errors    │
         │              └─────────────────┘
         │                       │
         ▼                       ▼
┌─────────────────────────────────────────┐
│        Global Exception Handler         │
│                                         │
│ • Catch all exceptions                  │
│ • Map to HTTP status codes             │
│ • Create standardized error responses  │
│ • Log errors for monitoring            │
└─────────────────────────────────────────┘
         │
         ▼
┌─────────────────┐
│   HTTP Client   │
│                 │
│ Standardized    │
│ Error Response  │
└─────────────────┘
```

### Exception Types

- **Infrastructure Exceptions**: Database connection errors, SQL exceptions
- **Validation Exceptions**: Missing parameters, type mismatches
- **Global Exception Handler**: Centralized error handling and response formatting

## 💾 Database Schema

### Inventory Table Structure

```sql
CREATE TABLE t_inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sku_code VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Database Migration

The application uses Liquibase for database schema management:

```bash
# Run migrations
./gradlew update

# Rollback migrations
./gradlew rollback -PliquibaseCommandValue=1
```

## 🔄 Clean Architecture Benefits Achieved

1. **Independence**: Business logic is independent of frameworks, UI, and external agencies
2. **Testability**: Easy to test business rules without database or external services
3. **Flexibility**: Easy to change frameworks, databases, or external services
4. **Maintainability**: Clear separation of concerns makes the code easier to understand and modify
5. **Scalability**: Architecture supports scaling individual components independently

## 🏃‍♂️ Business Logic Flow

### Add Inventory Flow
1. **Controller** receives HTTP request and validates input
2. **Mapper** converts DTO to domain model
3. **Service** checks if SKU exists and applies business rules
4. **Repository** persists or updates inventory data
5. **Response** returns updated inventory information

### Check Stock Flow
1. **Controller** receives stock check request
2. **Mapper** converts request parameters to domain model
3. **Service** validates stock availability against required quantity
4. **Repository** queries database for stock information
5. **Response** returns boolean indicating stock availability

## 🚀 Future Enhancements

- **Event Sourcing**: Add domain events for inventory changes
- **Caching**: Implement Redis for frequently accessed inventory data
- **Monitoring**: Add metrics and health checks
- **Security**: Implement authentication and authorization
- **API Versioning**: Support multiple API versions
- **Batch Operations**: Support bulk inventory updates 