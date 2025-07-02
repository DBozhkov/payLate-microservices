package org.payLate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${payment-service.url}")
    private String paymentServiceUrl;

    @Value("${messaging-service.url}")
    private String messagingServiceUrl;

    @Bean
    public WebClient paymentServiceWebClient() {
        return WebClient.builder()
                .baseUrl(paymentServiceUrl)
                .build();
    }

    @Bean
    public WebClient messagingServiceWebClient() {
        return WebClient.builder()
                .baseUrl(messagingServiceUrl)
                .build();
    }
}