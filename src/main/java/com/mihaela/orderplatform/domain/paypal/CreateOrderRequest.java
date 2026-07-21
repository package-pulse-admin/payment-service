package com.mihaela.orderplatform.domain.paypal;

import java.util.List;

public record CreateOrderRequest(
        String intent,
        List<PurchaseUnit> purchase_units
) {
}
