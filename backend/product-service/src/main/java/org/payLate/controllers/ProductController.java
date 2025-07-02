package org.payLate.controllers;

import org.payLate.requestModels.AddProductRequest;
import org.payLate.services.CSVService;
import org.payLate.services.OlxScraperService;
import org.payLate.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;

    private OlxScraperService olxScraperService;

    private CSVService csvService;

    @Autowired
    public ProductController(ProductService productService, CSVService csvService, OlxScraperService olxScraperService) {
        this.productService = productService;
        this.csvService = csvService;
        this.olxScraperService = olxScraperService;
    }

    @PostMapping("/{partner}/add")
    public ResponseEntity<?> addProduct(
            @PathVariable String partner,
            @RequestBody AddProductRequest addProductRequest) {
        try {
            productService.addProductForPartner(partner, addProductRequest);
            return ResponseEntity.ok("Product added successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to add product: " + e.getMessage());
        }
    }
}
