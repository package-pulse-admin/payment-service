package com.mihaela.orderplatform.enums;

import java.util.Arrays;

public enum Currency {

    EUR,
    USD,
    GBP,
    JPY,
    CNY,
    CHF,
    CAD,
    AUD,
    INR,
    KRW,
    BRL,
    RUB,
    MXN,
    SGD,
    HKD,
    NZD,
    SEK,
    NOK,
    DKK,
    BGN,
    UNKNOWN;

    public static Currency fromValue(String value) {
        return Arrays.stream(values())
                .filter(currency -> currency.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unsupported currency: " + value
                        )
                );
    }
}