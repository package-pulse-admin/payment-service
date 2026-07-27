package com.mihaela.orderplatform.domain.transactions;

import java.math.BigDecimal;

public record TransactionCreatedEvent(
        Long transactionId,
        Long orderId,
        String customerId,
        BigDecimal amount,
        String currency
) {
}
