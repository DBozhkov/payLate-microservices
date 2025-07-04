package org.payLate.services;

import org.payLate.dto.OrderItemRequest;
import org.payLate.dto.UserOrderRequest;
import org.payLate.dto.ProductDTO;
import org.payLate.entity.Cart;
import org.payLate.entity.CartItem;
import org.payLate.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final WebClient productServiceWebClient;
    private final WebClient orderServiceWebClient;

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    @Autowired
    public CartService(
            CartRepository cartRepository,
            @Qualifier("productServiceWebClient") WebClient productServiceWebClient,
            @Qualifier("orderServiceWebClient") WebClient orderServiceWebClient
    ) {
        this.cartRepository = cartRepository;
        this.productServiceWebClient = productServiceWebClient;
        this.orderServiceWebClient = orderServiceWebClient;
    }

    public void addToCart(String token, String userEmail, Long productId, String partner) throws Exception {
        Boolean productExists = productServiceWebClient.get()
                .uri("/api/products/{partner}/{id}/exists", partner, productId)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (Boolean.FALSE.equals(productExists)) {
            throw new Exception("Product not found in product-service");
        }

        Optional<Cart> cartOptional = cartRepository.findByUserEmail(userEmail);
        Cart cart = cartOptional.orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserEmail(userEmail);
            return newCart;
        });

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId) &&
                        (partner == null || partner.equals(item.getPartner())))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + 1);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProductId(productId);
            cartItem.setQuantity(1);
            cartItem.setPartner(partner);
            cartItem.setCart(cart);
            cart.getItems().add(cartItem);
        }

        cartRepository.save(cart);
    }

    public List<ProductDTO> getCartItems(String userEmail, String token) {
        Optional<Cart> cartOptional = cartRepository.findByUserEmail(userEmail);
        if (cartOptional.isEmpty()) {
            logger.info("Cart not found for user: {}", userEmail);
            return List.of();
        }

        Cart cart = cartOptional.get();

        // Remove any cart items referencing missing products
        boolean removedAny = cart.getItems().removeIf(item ->
                fetchProductDetails(item.getProductId(), item.getPartner(), token) == null
        );
        if (removedAny) {
            cartRepository.save(cart);
        }

        // Now collect product details for remaining valid items
        return cart.getItems().stream()
                .map(item -> fetchProductDetails(item.getProductId(), item.getPartner(), token))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void createOrder(String userEmail, String token) throws Exception {

        List<Cart> allCarts = cartRepository.findAll();
        System.out.println("=== All carts in DB ===");
        for (Cart c : allCarts) {
            System.out.println("Cart for userEmail: [" + c.getUserEmail() + "]");
        }
        System.out.println("Requested userEmail: [" + userEmail + "]");

        Optional<Cart> cartOptional = cartRepository.findByUserEmailIgnoreCase(userEmail);
        if (cartOptional.isEmpty()) {
            throw new Exception("Cart not found");
        }

        Cart cart = cartOptional.get();
        System.out.println("Cart found for user: " + userEmail);
        System.out.println("Cart items size: " + (cart.getItems() != null ? cart.getItems().size() : "null"));
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new Exception("Cart is empty, cannot create order");
        }
        for (CartItem item : cart.getItems()) {
            System.out.println("CartItem: productId=" + item.getProductId() + ", quantity=" + item.getQuantity());
        }

        List<OrderItemRequest> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItemRequest orderItemRequest = new OrderItemRequest();
            orderItemRequest.setProductId(cartItem.getProductId());
            orderItemRequest.setQuantity(cartItem.getQuantity());
            orderItemRequest.setPartner(cartItem.getPartner());

            ProductDTO product = fetchProductDetails(cartItem.getProductId(), cartItem.getPartner(), token);
            if (product != null) {
                orderItemRequest.setImgUrl(product.getImgUrl());
                orderItemRequest.setPrice(product.getPrice());
            }

            return orderItemRequest;
        }).collect(Collectors.toList());

        UserOrderRequest userOrderRequest = new UserOrderRequest();
        userOrderRequest.setUserEmail(userEmail);
        userOrderRequest.setItems(orderItems);

        try {
            orderServiceWebClient.post()
                    .uri("/api/orders")
                    .bodyValue(userOrderRequest)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (WebClientResponseException e) {
            System.out.println("Order Service returned status: " + e.getRawStatusCode());
            System.out.println("Order Service response body: " + e.getResponseBodyAsString());
            throw new Exception("Order service error: " + e.getResponseBodyAsString());
        }

        cartRepository.delete(cart);
    }

    private ProductDTO fetchProductDetails(Long productId, String partner, String token) {
        try {
            return productServiceWebClient.get()
                    .uri("/api/products/{partner}/{id}", partner, productId)
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(ProductDTO.class)
                    .block();
        } catch (Exception e) {
            logger.error("Failed to fetch product details for productId={}, partner={}: {}", productId, partner, e.getMessage());
            return null;
        }
    }

    public void removeFromCart(String userEmail, Long productId) throws Exception {
        Optional<Cart> cartOptional = cartRepository.findByUserEmail(userEmail);
        if (cartOptional.isEmpty()) {
            throw new Exception("Cart not found");
        }

        Cart cart = cartOptional.get();

        boolean itemRemoved = cart.getItems().removeIf(item -> item.getProductId().equals(productId));

        if (!itemRemoved) {
            throw new Exception("Product not found in the cart");
        }

        cartRepository.save(cart);
    }

    public void clearCart(String userEmail) throws Exception {
        Optional<Cart> cartOptional = cartRepository.findByUserEmail(userEmail);
        if (cartOptional.isEmpty()) {
            throw new Exception("Cart not found");
        }

        Cart cart = cartOptional.get();
        cartRepository.delete(cart);
    }
}