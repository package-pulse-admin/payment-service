package com.mihaela.orderplatform.service.payment;

import com.mihaela.orderplatform.domain.transactions.TransactionCreatedEvent;

public interface PaymentService {

    void processPayment(TransactionCreatedEvent event);
}
