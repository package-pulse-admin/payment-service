package com.mihaela.orderplatform.domain.paypal;

public record Amount(
        String currency_code,
        String value
) {
}
