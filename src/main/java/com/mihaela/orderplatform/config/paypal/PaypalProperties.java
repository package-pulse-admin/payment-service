package com.mihaela.orderplatform.config.paypal;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment-service.payment.paypal")
public record PaypalProperties(
        String baseUrl,
        String clientId,
        String clientSecret
) {
}
