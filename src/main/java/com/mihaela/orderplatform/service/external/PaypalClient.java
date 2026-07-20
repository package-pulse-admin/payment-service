package com.mihaela.orderplatform.service.external;

import com.mihaela.orderplatform.config.paypal.PaypalProperties;
import com.mihaela.orderplatform.domain.paypal.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaypalClient {

    private final WebClient paypalWebClient;
    private final PaypalProperties properties;

    private static final String BASE_OAUTH_TOKEN_URL = "/v1/oauth2/token";
    private static final String GRANT_TYPE = "grant_type=client_credentials";
    private static final String BASE_CHECKOUT_ORDER_URL = "/v2/checkout/orders";

    public String getAccessToken() {
        return paypalWebClient
                .post()
                .uri(BASE_OAUTH_TOKEN_URL)
                .headers(headers -> headers.setBasicAuth(
                        properties.clientId(),
                        properties.clientSecret()
                ))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(GRANT_TYPE)
                .retrieve()
                .bodyToMono(PaypalAccessTokenResponse.class)
                .map(PaypalAccessTokenResponse::accessToken)
                .block();
    }

    public CreateOrderResponse createOrder(BigDecimal amount, String currency) {
        String token = getAccessToken();

        CreateOrderRequest request = buildOrderRequest(amount, currency);

        return paypalWebClient
                .post()
                .uri(BASE_CHECKOUT_ORDER_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CreateOrderResponse.class)
                .block();
    }

    private CreateOrderRequest buildOrderRequest(BigDecimal amount, String currency) {
        return new CreateOrderRequest("CAPTURE", List.of(new PurchaseUnit(new Amount(currency, amount.toString()))));
    }
}
