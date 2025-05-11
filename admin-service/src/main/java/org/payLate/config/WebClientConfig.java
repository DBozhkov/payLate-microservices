package org.payLate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${product-service.url}")
    private String productServiceUrl;

    @Value("${cart-service.url}")
    private String cartServiceUrl;

    @Value("${review-service.url}")
    private String reviewServiceUrl;

    @Bean
    public WebClient productServiceWebClient() {
        return WebClient.builder()
                .baseUrl(productServiceUrl)
                .build();
    }

    @Bean
    public WebClient cartServiceWebClient() {
        return WebClient.builder()
                .baseUrl(cartServiceUrl)
                .build();
    }

    @Bean
    public WebClient reviewServiceWebClient() {
        return WebClient.builder()
                .baseUrl(reviewServiceUrl)
                .build();
    }
}