package com.mihaela.orderplatform.config.paypal;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PaypalProperties.class)
public class PaypalConfig {
}
