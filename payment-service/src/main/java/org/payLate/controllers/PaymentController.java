package org.payLate.controllers;

import org.payLate.requestmodels.PaymentRequest;
import org.payLate.services.PaymentService;
import org.payLate.utils.JWTExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("/api/payment/secure")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPaymentIntent(@RequestBody PaymentRequest paymentRequest) {
        try {
            Map<String, Object> paymentIntent = paymentService.createPaymentIntent(paymentRequest);
            return ResponseEntity.ok(paymentIntent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/payment-complete")
    public ResponseEntity<String> stripePaymentComplete(@RequestHeader(value = "Authorization") String token) {
        try {
            String userEmail = JWTExtractor.payloadJWTExtraction(token, "\"sub\"");
            if (userEmail == null) {
                throw new IllegalArgumentException("User email is missing");
            }

            Long orderId = paymentService.createOrder(userEmail);

            paymentService.completePayment(userEmail, orderId);

            return ResponseEntity.ok("Payment completed successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error completing payment: " + e.getMessage());
        }
    }
}