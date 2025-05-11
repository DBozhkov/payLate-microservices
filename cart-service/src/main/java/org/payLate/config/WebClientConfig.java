package org.payLate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${product-service.url}")
    private String productServiceUrl;

    @Value("${order-service.url}")
    private String orderServiceUrl;

    @Bean
    public WebClient productServiceWebClient() {
        return WebClient.builder()
                .baseUrl(productServiceUrl)
                .build();
    }

    @Bean
    public WebClient orderServiceWebClient() {
        return WebClient.builder()
                .baseUrl(orderServiceUrl)
                .build();
    }
}