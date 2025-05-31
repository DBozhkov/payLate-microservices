package org.payLate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient productServiceWebClient(@Value("${product-service.url}") String productServiceUrl) {
        return WebClient.builder()
                .baseUrl(productServiceUrl)
                .build();
    }

    @Bean
    public WebClient cartServiceWebClient(@Value("${cart-service.url}") String cartServiceUrl) {
        return WebClient.builder()
                .baseUrl(cartServiceUrl)
                .build();
    }

    @Bean
    public WebClient reviewServiceWebClient(@Value("${review-service.url}") String reviewServiceUrl) {
        return WebClient.builder()
                .baseUrl(reviewServiceUrl)
                .build();
    }
}