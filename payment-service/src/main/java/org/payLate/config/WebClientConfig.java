package org.payLate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${order-service.url}")
    private String orderServiceUrl;

    @Value("${cart-service.url}")
    private String cartServiceUrl;

    @Value("${stripe.api.base-url}")
    private String stripeBaseUrl;

    @Bean
    public WebClient orderServiceWebClient() {
        return WebClient.builder()
                .baseUrl(orderServiceUrl)
                .build();
    }

    @Bean
    public WebClient cartServiceWebClient() {
        return WebClient.builder()
                .baseUrl(cartServiceUrl)
                .build();
    }

    @Bean
    public WebClient stripeWebClient() {
        return WebClient.builder()
                .baseUrl(stripeBaseUrl)
                .defaultHeader("Authorization", "Bearer " + "{stripe.key.secret}")
                .build();
    }
}