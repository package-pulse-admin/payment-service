package com.mihaela.orderplatform.domain.payment;

import com.mihaela.orderplatform.enums.Currency;
import com.mihaela.orderplatform.enums.TransactionStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record PaymentNotificationEvent(UUID eventId, Instant occurredAt, Long paymentId, Long transactionId, Long orderId,
                                       String customerId, BigDecimal amount, Currency currency, TransactionStatus status) {
}
