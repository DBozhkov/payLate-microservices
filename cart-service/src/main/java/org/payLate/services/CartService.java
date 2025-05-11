package org.payLate.services;

import org.payLate.dto.OrderItemRequest;
import org.payLate.dto.UserOrderRequest;
import org.payLate.dto.ProductDTO;
import org.payLate.entity.Cart;
import org.payLate.entity.CartItem;
import org.payLate.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final WebClient productServiceWebClient;
    private final WebClient orderServiceWebClient;

    @Autowired
    public CartService(CartRepository cartRepository,
                       WebClient productServiceWebClient,
                       WebClient orderServiceWebClient) {
        this.cartRepository = cartRepository;
        this.productServiceWebClient = productServiceWebClient;
        this.orderServiceWebClient = orderServiceWebClient;
    }

    public void addToCart(String userEmail, Long productId, String partner) throws Exception {
        Boolean productExists = productServiceWebClient.get()
                .uri("/api/products/{partner}/{id}/exists", partner, productId)
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

    public List<ProductDTO> getCartItems(String userEmail) {
        Optional<Cart> cartOptional = cartRepository.findByUserEmail(userEmail);
        if (cartOptional.isEmpty()) {
            System.out.println("Cart not found for user: " + userEmail);
            return List.of();
        }

        return cartOptional.get().getItems().stream()
                .map(item -> fetchProductDetails(item.getProductId(), item.getPartner()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void createOrder(String userEmail) throws Exception {
        Optional<Cart> cartOptional = cartRepository.findByUserEmail(userEmail);
        if (cartOptional.isEmpty()) {
            throw new Exception("Cart not found");
        }

        Cart cart = cartOptional.get();

        List<OrderItemRequest> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItemRequest orderItemRequest = new OrderItemRequest();
            orderItemRequest.setProductId(cartItem.getProductId());
            orderItemRequest.setQuantity(cartItem.getQuantity());
            orderItemRequest.setPartner(cartItem.getPartner());

            ProductDTO product = fetchProductDetails(cartItem.getProductId(), cartItem.getPartner());
            if (product != null) {
                orderItemRequest.setImgUrl(product.getImgUrl());
                orderItemRequest.setPrice(product.getPrice());
            }

            return orderItemRequest;
        }).collect(Collectors.toList());

        UserOrderRequest userOrderRequest = new UserOrderRequest();
        userOrderRequest.setUserEmail(userEmail);
        userOrderRequest.setItems(orderItems);

        orderServiceWebClient.post()
                .uri("/api/orders")
                .bodyValue(userOrderRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        cartRepository.delete(cart);
    }

    private ProductDTO fetchProductDetails(Long productId, String partner) {
        return productServiceWebClient.get()
                .uri("/api/products/{partner}/{id}", partner, productId)
                .retrieve()
                .bodyToMono(ProductDTO.class)
                .block();
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