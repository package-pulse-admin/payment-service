package com.mihaela.orderplatform.service.payment;

import com.mihaela.orderplatform.domain.payment.PaymentNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaPaymentEventProducer
        implements PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${payment.kafka.payment-notification-topic}")
    private String paymentNotificationTopic;

    @Override
    public void publishPaymentNotification(PaymentNotificationEvent event) {
        kafkaTemplate.send(paymentNotificationTopic, event.transactionId().toString(), event);
        log.info("Published payment completed event. transactionId={}", event.transactionId());
    }
}
