package org.payLate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.payLate.entity.UserOrder;
import org.payLate.requestModels.UserOrderRequest;
import org.payLate.services.OrderService;
import org.payLate.utils.JWTExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody UserOrderRequest orderRequest) {
        try {
            System.out.println("Received order request: " + new ObjectMapper().writeValueAsString(orderRequest));
            UserOrder order = orderService.saveUserOrder(orderRequest);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating order: " + e.getMessage());
        }
    }

    @GetMapping("/previous")
    public ResponseEntity<List<UserOrder>> getUserOrders(@RequestHeader(value = "Authorization") String token) {
        try {
            String userEmail = JWTExtractor.payloadJWTExtraction(token, "\"sub\"");
            if (userEmail == null) {
                throw new Exception("User email is missing");
            }

            List<UserOrder> orders = orderService.getUserOrders(userEmail);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserOrder> getOrderById(@PathVariable("id") Long id) {
        try {
            UserOrder order = orderService.getOrderById(id);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<List<UserOrder>> getPendingOrders() {
        try {
            List<UserOrder> pendingOrders = orderService.getPendingOrders();

            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println("Pending Orders JSON: " + objectMapper.writeValueAsString(pendingOrders));

            return ResponseEntity.ok(pendingOrders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/paid-order")
    public ResponseEntity<String> markOrderAsPaid(@RequestBody Map<String, Object> request) {
        try {
            Long orderId = Long.valueOf(request.get("orderId").toString());
            String userEmail = request.get("userEmail").toString();

            orderService.markOrderAsPaid(orderId, userEmail);
            return ResponseEntity.ok("Order marked as paid successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/complete-order")
    public ResponseEntity<String> completeOrder(@RequestBody Map<String, Object> request) {
        try {
            Long orderId = Long.valueOf(request.get("orderId").toString());
            String userEmail = request.get("userEmail").toString();

            orderService.markOrderAsPaid(orderId, userEmail);
            return ResponseEntity.ok("Order completed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error completing order: " + e.getMessage());
        }
    }
}