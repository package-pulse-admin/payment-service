package com.mihaela.orderplatform.service.payment;

import com.mihaela.orderplatform.domain.payment.PaymentNotificationEvent;

public interface PaymentEventProducer {

    void publishPaymentNotification(PaymentNotificationEvent event);
}
