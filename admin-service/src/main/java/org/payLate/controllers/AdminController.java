package org.payLate.controllers;

import org.payLate.requestModels.AddProductRequest;
import org.payLate.services.AdminService;
import org.payLate.utils.JWTExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/secure/add/product")
    public void postProduct(@RequestHeader(value = "Authorization") String token,
                            @RequestBody AddProductRequest addProductRequest) throws Exception {
        String admin = JWTExtractor.payloadJWTExtraction(token, "\"userType\"");
        if (admin == null || !admin.equals("admin")) {
            throw new Exception("Administration only!!!");
        }

        adminService.addProduct(addProductRequest);
    }

    @DeleteMapping("/secure/delete/product")
    public void deleteProduct(@RequestHeader(value = "Authorization") String token,
                              @RequestParam Long productId,
                              @RequestParam(required = false) String partner) throws Exception {
        String userType = JWTExtractor.payloadJWTExtraction(token, "\"userType\"");
        if (userType == null || !userType.equals("admin")) {
            throw new Exception("Administration only!!!");
        }

        adminService.deleteProduct(productId, partner);
    }

    @PostMapping("/pay-late")
    public ResponseEntity<String> saveOrderForPayLate(@RequestHeader(value = "Authorization") String token,
                                                      @RequestParam String userEmail) {
        try {
            adminService.createOrderForPayLate(token, userEmail);
            return ResponseEntity.ok("Order saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving order: " + e.getMessage());
        }
    }
}