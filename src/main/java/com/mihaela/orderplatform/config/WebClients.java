package com.mihaela.orderplatform.config;

import com.mihaela.orderplatform.config.paypal.PaypalProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClients {

    @Bean
    public WebClient paypalWebClient(PaypalProperties properties) {
        return WebClient.builder()
                .baseUrl(properties.baseUrl())
                .build();
    }
}
