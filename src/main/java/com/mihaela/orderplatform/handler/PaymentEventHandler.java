package com.mihaela.orderplatform.handler;

import com.mihaela.orderplatform.domain.transactions.TransactionCreatedEvent;
import com.mihaela.orderplatform.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventHandler {

    private final PaymentService paymentService;

    public void handle(TransactionCreatedEvent event) {
        paymentService.processPayment(event);
    }
}
