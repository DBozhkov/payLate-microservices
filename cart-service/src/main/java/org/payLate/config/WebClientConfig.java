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
    public WebClient orderServiceWebClient(@Value("${order-service.url}") String orderServiceUrl) {
        return WebClient.builder()
                .baseUrl(orderServiceUrl)
                .build();
    }
}