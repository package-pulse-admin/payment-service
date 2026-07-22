package com.mihaela.orderplatform.kafka;

import com.mihaela.orderplatform.domain.transactions.TransactionCreatedEvent;
import com.mihaela.orderplatform.handler.PaymentEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionCreatedEventListener {

    private final PaymentEventHandler paymentEventHandler;

    @KafkaListener(
            topics = "${payment-service.payment.kafka.transaction-created-topic}",
            groupId = "${payment-service.payment.kafka.group-id}"
    )
    public void listen(TransactionCreatedEvent event) {
        log.info("Received transaction created event. transactionId={}", event.transactionId());
        paymentEventHandler.handle(event);
    }
}
