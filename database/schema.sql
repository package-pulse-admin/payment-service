CREATE DATABASE IF NOT EXISTS order_platform;

USE order_platform;

CREATE TABLE IF NOT EXISTS customer_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique order identifier',
    customer_id VARCHAR(100) NOT NULL COMMENT 'Reference to customer',
    amount DECIMAL(10,2) NOT NULL COMMENT 'Original order amount',
    currency VARCHAR(7) NOT NULL DEFAULT 'UNKNOWN' COMMENT 'ISO 4217 currency code',
    status VARCHAR(50) NOT NULL DEFAULT 'CREATED' COMMENT 'Order status',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_customer_id(customer_id),
    INDEX idx_order_status(status)
) COMMENT='Stores customer orders';

CREATE TABLE IF NOT EXISTS customer_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique transaction identifier',
    order_id BIGINT NOT NULL COMMENT 'Reference to customer order',
    customer_id VARCHAR(100) NOT NULL COMMENT 'Reference to customer',
    amount DECIMAL(10,2) NOT NULL COMMENT 'Transaction amount',
    currency VARCHAR(7) NOT NULL DEFAULT 'UNKNOWN' COMMENT 'ISO currency code',
    status VARCHAR(50) NOT NULL DEFAULT 'CREATED' COMMENT 'Transaction status',
    total_amount DECIMAL(15,2) NOT NULL COMMENT 'Final charged amount including fees',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_transaction_order
        FOREIGN KEY (order_id)
        REFERENCES customer_orders(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    INDEX idx_transaction_order_id(order_id),
    INDEX idx_transaction_customer_id(customer_id),
    INDEX idx_transaction_status(status)
) COMMENT='Stores financial transactions for orders';

CREATE TABLE IF NOT EXISTS transaction_fees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique fee identifier',
    transaction_id BIGINT NOT NULL COMMENT 'Reference to transaction',
    fee_type VARCHAR(30) NOT NULL  COMMENT 'PROCESSING, TAX, FX, SHIPPING etc.',
    amount DECIMAL(15,2) NOT NULL COMMENT 'Fee amount',
    description VARCHAR(255) COMMENT 'Optional fee description',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_fee_transaction
        FOREIGN KEY (transaction_id)
        REFERENCES customer_transactions(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    INDEX idx_fee_transaction_id(transaction_id),
    INDEX idx_fee_type(fee_type)
) COMMENT='Stores fees related to transactions';

CREATE TABLE IF NOT EXISTS payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique fee identifier',
    transaction_id BIGINT NOT NULL COMMENT 'Reference to transaction',
    order_id BIGINT NOT NULL COMMENT 'Reference to customer order',
    customer_id VARCHAR(100) NOT NULL COMMENT 'Reference to customer',
    amount DECIMAL(10,2) NOT NULL COMMENT 'Payment amount',
    currency VARCHAR(7) NOT NULL DEFAULT 'UNKNOWN' COMMENT 'ISO currency code',
    provider VARCHAR(50) NOT NULL DEFAULT 'PAYPAL' COMMENT 'Payment provider used for processing the payment',
    paypal_payment_id VARCHAR(255) COMMENT 'Payment identifier returned by PayPal',,
    status VARCHAR(50) NOT NULL DEFAULT 'CREATED' COMMENT 'Payment status',
    failure_reason VARCHAR(500) COMMENT 'Reason why payment failed',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Payment creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Payment last update timestamp',
    CONSTRAINT fk_payment_transaction
        FOREIGN KEY (transaction_id)
        REFERENCES customer_transactions(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_payment_order
        FOREIGN KEY (order_id)
        REFERENCES customer_orders(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT uq_payment_transaction UNIQUE(transaction_id),

    INDEX idx_payment_transaction_id(transaction_id),
    INDEX idx_payment_order_id(order_id),
    INDEX idx_payment_customer_id(customer_id),
    INDEX idx_payment_status(status),
    INDEX idx_payment_paypal_id(paypal_payment_id)
) COMMENT='Stores payment records created from customer transactions';