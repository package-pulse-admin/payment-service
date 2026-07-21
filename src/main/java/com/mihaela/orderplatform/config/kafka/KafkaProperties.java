package com.mihaela.orderplatform.config.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment-service.payment.kafka")
public record KafkaProperties(
        String transactionCreatedTopic,
        String groupId
) {
}
