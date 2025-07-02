package org.payLate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient orderServiceWebClient(@Value("${order-service.url}") String orderServiceUrl) {
        return WebClient.builder()
                .baseUrl(orderServiceUrl)
                .build();
    }

    @Bean
    public WebClient cartServiceWebClient(@Value("${cart-service.url}") String cartServiceUrl) {
        return WebClient.builder()
                .baseUrl(cartServiceUrl)
                .build();
    }

    @Bean
    public WebClient stripeWebClient(
            @Value("${stripe.api.base-url}") String stripeBaseUrl,
            @Value("${stripe.key.secret}") String stripeSecret
    ) {
        return WebClient.builder()
                .baseUrl(stripeBaseUrl)
                .defaultHeader("Authorization", "Bearer " + stripeSecret)
                .build();
    }
}