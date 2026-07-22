package com.mihaela.orderplatform.mapper;

import com.mihaela.orderplatform.domain.payment.Payment;
import com.mihaela.orderplatform.domain.payment.PaymentNotificationEvent;
import com.mihaela.orderplatform.domain.transactions.TransactionCreatedEvent;
import com.mihaela.orderplatform.enums.Currency;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransactionPaymentMapper {

    public Payment fromTransactionCreatedEvent(TransactionCreatedEvent event) {
        Payment payment = new Payment();

        payment.setTransactionId(event.transactionId());
        payment.setOrderId(event.orderId());
        payment.setCustomerId(event.customerId());
        payment.setAmount(event.amount());
        payment.setCurrency(Currency.valueOf(event.currency()));

        return payment;
    }

    public PaymentNotificationEvent fromPayment(Payment payment){
        return PaymentNotificationEvent.builder()
                .eventId(UUID.randomUUID())
                .occurredAt(payment.getCreatedAt())
                .paymentId(payment.getId())
                .transactionId(payment.getTransactionId())
                .orderId(payment.getOrderId())
                .customerId(payment.getCustomerId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .build();
    }
}
