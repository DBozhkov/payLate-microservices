package org.payLate.controllers;

import org.payLate.dto.ProductDTO;
import org.payLate.services.CartService;
import org.payLate.utils.JWTExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ProductDTO>> getCartItems(
            @RequestHeader(value = "Authorization") String token) {
        try {
            String userEmail = JWTExtractor.payloadJWTExtraction(token, "\"sub\"");
            if (userEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            List<ProductDTO> items = cartService.getCartItems(userEmail, token);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/remove")
    public void removeFromCart(@RequestHeader(value = "Authorization") String token,
                               @RequestParam("productId") Long productId) throws Exception {
        String userEmail = JWTExtractor.payloadJWTExtraction(token, "\"sub\"");
        if (userEmail == null) {
            throw new Exception("User email is missing");
        }
        cartService.removeFromCart(userEmail, productId);
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody String userEmail) {
        try {
            String extractedEmail = JWTExtractor.payloadJWTExtraction(token, "\"sub\"");
            System.out.println("JWT extracted email: [" + extractedEmail + "], userEmail param: [" + userEmail + "]");
            if (extractedEmail == null || !extractedEmail.equals(userEmail)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User email mismatch or missing: " + extractedEmail + " vs " + userEmail);
            }
            cartService.createOrder(userEmail, token);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
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