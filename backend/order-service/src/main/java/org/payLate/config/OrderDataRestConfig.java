package org.payLate.config;

import org.payLate.entity.UserOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class OrderDataRestConfig implements RepositoryRestConfigurer {

//    private String allAllowedOrigins = "https://localhost:3000";

    private String[] allAllowedOrigins = {
            "https://paylate.com",
            "https://localhost:3000",
            "https://product-service.paylate.com",
            "https://admin-service.paylate.com",
            "https://cart-service.paylate.com",
            "https://messaging-service.paylate.com",
            "https://order-service.paylate.com",
            "https://payment-service.paylate.com",
            "https://review-service.paylate.com",
    };

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        HttpMethod[] unsupportedActions = {HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE, HttpMethod.PUT};

        config.exposeIdsFor(UserOrder.class);

        disableHttpMethods(UserOrder.class, config, unsupportedActions);

        cors.addMapping("/**")
                .allowedOrigins(allAllowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true);
    }

    private void disableHttpMethods(Class<?> theClass, RepositoryRestConfiguration config, HttpMethod[] unsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedActions))
                .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedActions));
    }
}