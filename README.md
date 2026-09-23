# warehouse-service

WMS Increment 1 - Warehouse master microservice.

- Spring Boot: 4.0.8
- Project version: 3.8.0
- Java: 21
- Persistence: Spring Data JPA + PostgreSQL
- Migrations: Flyway
- Security: Spring Security foundation
- Events: Kafka foundation
- API docs: OpenAPI/Swagger
- Observability: Actuator
- Architecture: Controller -> Service/Application -> Domain -> Repository

## API

POST `/api/v1/warehouses`
GET `/api/v1/warehouses/{id}`

Swagger UI: `/swagger-ui/index.html`
Health: `/actuator/health`

## Example

POST `/api/v1/warehouses`

```json
{
  "warehouseCode": "WH-001",
  "name": "Main Warehouse",
  "address": "Warehouse address",
  "status": "ACTIVE"
}
```

## Run

Set PostgreSQL connection properties in `application.yml`, then:

`mvn spring-boot:run`
