package org.payLate.controllers;


import org.payLate.dto.ProductDTO;
import org.payLate.services.CartService;
import org.payLate.utils.JWTExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public void addToCart(@RequestHeader(value = "Authorization") String token,
                          @RequestParam("productId") Long productId,
                          @RequestParam("partner") String partner) throws Exception {
        String userEmail = JWTExtractor.payloadJWTExtraction(token, "\"sub\"");
        if (userEmail == null) {
            throw new Exception("User email is missing");
        }
        cartService.addToCart(token, userEmail, productId, partner);
    }

    @GetMapping
    public List<ProductDTO> getCartItems(@RequestHeader(value = "Authorization") String token) throws Exception {
        String userEmail = JWTExtractor.payloadJWTExtraction(token, "\"sub\"");
        if (userEmail == null) {
            throw new Exception("User email is missing");
        }
        System.out.println("Fetched userEmail: " + userEmail);
        return cartService.getCartItems(userEmail);
    }

    @DeleteMapping("/remove")
    public void removeFromCart(@RequestHeader(value = "Authorization") String token,
                               @RequestParam Long productId) throws Exception {
        String userEmail = JWTExtractor.payloadJWTExtraction(token, "\"sub\"");
        if (userEmail == null) {
            throw new Exception("User email is missing");
        }
        cartService.removeFromCart(userEmail, productId);
    }

    @DeleteMapping("/clear")
    public void clearCart(@RequestHeader(value = "Authorization") String token) throws Exception {
        String userEmail = JWTExtractor.payloadJWTExtraction(token, "\"sub\"");
        if (userEmail == null) {
            throw new Exception("User email is missing");
        }
        cartService.clearCart(userEmail);
    }
}