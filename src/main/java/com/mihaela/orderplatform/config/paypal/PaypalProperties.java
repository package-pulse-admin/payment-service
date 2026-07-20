package com.mihaela.orderplatform.config.paypal;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "paypal")
public record PaypalProperties(
        String baseUrl,
        String clientId,
        String clientSecret
) {
}
