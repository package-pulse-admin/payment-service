# Order Platform Overview
![![order_platform_overview.png](..%2Forder-service%2Forder_platform_overview.png)img_2.png](order_platform_overview.png)

# payment-service

## Overview

The Payment Service is responsible for processing customer transaction events and executing payments through PayPal Sandbox.
It consumes transaction events published by the Transaction Service, creates payment records, communicates with the external PayPal API,
updates the payment status, and publishes payment notification events for downstream services.

## Architecture

Customer Orders (MySQL) -> Debezium CDC -> Kafka -> Transaction Service -> Payment Service

## Features
- Consumes transaction events from Kafka
- Prevents duplicate payment processing
- Maps transaction events to payment entities
- Persists payment records in MySQL
- Integrates with PayPal Sandbox using Spring WebClient
- Updates payment status (PROCESSING, PAID, PAYMENT_FAILED)
- Stores PayPal payment identifiers
- Publishes payment notification events for downstream microservices
- Exposes health and Prometheus metrics

## Technology Stack

- Java 21
- Spring Boot 3
- Spring Data JPA
- MySQL
- Kafka
- Debezium
- Docker
- Maven

## Configuration

The application uses environment variables.
| Variable | Description |
|-----------|-------------|
| DB_HOST | MySQL host |
| DB_PORT | MySQL port |
| DB_USERNAME | Database username |
| DB_PASSWORD | Database password |
| KAFKA_BOOTSTRAP_SERVERS | Kafka broker |
| PAYPAL_CLIENT_ID | PayPal Sandbox Client ID |
| PAYPAL_CLIENT_SECRET | PayPal Sandbox Client Secret |
| PAYPAL_BASE_URL | PayPal API base URL |

## Database

| Column | Description |
|---------|-------------|
| id | Payment identifier |
| transaction_id | Associated transaction |
| order_id | Associated order |
| customer_id | Customer identifier |
| amount | Payment amount |
| currency | Payment currency |
| provider | Payment provider (PayPal) |
| paypal_payment_id | Identifier returned by PayPal |
| status | Payment status |
| failure_reason | Failure reason, if applicable |
| created_at | Creation timestamp |
| updated_at | Last update timestamp |

## Kafka

Consumes 'order-platform.customer_transactions'
Produces 'order-platform.payment_process'

## Payment Processing

The service performs the following steps:

1. Receives a transaction event from Kafka.
2. Verifies that a payment for the transaction has not already been processed.
3. Creates a payment record with **PROCESSING** status.
4. Sends a payment request to PayPal Sandbox.
5. Updates the payment status to **PAID** or **PAYMENT_FAILED**.
6. Publishes a payment notification event for downstream services.

## Running the Application

```bash
docker compose up -d
```

## API Documentation

Swagger UI
http://localhost:8082/swagger-ui/index.html

## Event Flow

1. Transaction Service publishes a `customer_transactions` event.
2. Payment Service consumes the event from Kafka.
3. A payment record is created and stored.
4. Payment Service invokes the PayPal Sandbox API.
5. The payment status is updated.
6. A `payment.process` event is published.
7. Notification Service consumes the event and sends a payment confirmation email.