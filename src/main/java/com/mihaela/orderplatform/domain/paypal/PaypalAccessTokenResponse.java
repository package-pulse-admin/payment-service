package com.mihaela.orderplatform.domain.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaypalAccessTokenResponse(

        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("token_type")
        String tokenType,

        @JsonProperty("expires_in")
        Long expiresIn

) {
}
