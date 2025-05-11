package org.payLate.services;

import org.payLate.requestModels.AddProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Transactional
public class AdminService {

    private final WebClient productServiceWebClient;
    private final WebClient cartServiceWebClient;
    private final WebClient reviewServiceWebClient;

    public AdminService(WebClient productServiceWebClient,
                        WebClient cartServiceWebClient,
                        WebClient reviewServiceWebClient) {
        this.productServiceWebClient = productServiceWebClient;
        this.cartServiceWebClient = cartServiceWebClient;
        this.reviewServiceWebClient = reviewServiceWebClient;
    }

    public void addProduct(AddProductRequest addProductRequest) throws Exception {
        productServiceWebClient.post()
                .uri("/api/products/{partner}/add", addProductRequest.getProductType().toLowerCase())
                .bodyValue(addProductRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void deleteProduct(Long productId, String partner) throws Exception {
        Boolean productExists = productServiceWebClient.get()
                .uri("/api/products/{partner}/{id}/exists", partner.toLowerCase(), productId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (Boolean.FALSE.equals(productExists)) {
            throw new Exception("Product not found in product-service!");
        }

        cartServiceWebClient.delete()
                .uri("/api/cart/remove-product/{productId}", productId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        reviewServiceWebClient.delete()
                .uri("/api/reviews/delete-by-product/{productId}", productId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        productServiceWebClient.delete()
                .uri("/api/products/{partner}/{id}", partner.toLowerCase(), productId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void createOrderForPayLate(String token, String userEmail) throws Exception {
        cartServiceWebClient.post()
                .uri("/api/cart/create-order")
                .header("Authorization", token)
                .bodyValue(userEmail)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}