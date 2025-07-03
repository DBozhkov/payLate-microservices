package org.payLate.services;

import org.payLate.requestModels.AddProductRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Transactional
public class AdminService {

    private final WebClient productServiceWebClient;
    private final WebClient cartServiceWebClient;
    private final WebClient reviewServiceWebClient;

    public AdminService(
            @Qualifier("productServiceWebClient") WebClient productServiceWebClient,
            @Qualifier("cartServiceWebClient") WebClient cartServiceWebClient,
            @Qualifier("reviewServiceWebClient") WebClient reviewServiceWebClient
    ) {
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
        partner = (partner == null) ? "" : partner.toLowerCase();

        Boolean productExists = null;
        try {
            productExists = productServiceWebClient.get()
                    .uri("/api/products/{partner}/{id}/exists", partner, productId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (Exception e) {
            System.err.println("[admin-service] Could not verify product existence: " + e.getMessage());
        }

        if (Boolean.FALSE.equals(productExists)) {
            System.err.println("[admin-service] Product not found in product-service, skipping downstream deletes.");
        }

        try {
            cartServiceWebClient.delete()
                    .uri("/api/cart/remove-product/{productId}", productId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            System.err.println("[admin-service] cart-service remove failed: " + e.getMessage());
        }

        try {
            reviewServiceWebClient.delete()
                    .uri("/api/reviews/delete-by-product/{productId}", productId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            System.err.println("[admin-service] review-service remove failed: " + e.getMessage());
        }

        try {
            productServiceWebClient.delete()
                    .uri("/api/products/{partner}/{id}", partner, productId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            System.err.println("[admin-service] product-service delete failed: " + e.getMessage());
        }
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