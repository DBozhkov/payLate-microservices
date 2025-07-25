package org.payLate.controllers;

import org.payLate.dto.AliExpressProductDTO;
import org.payLate.dto.AmazonProductDTO;
import org.payLate.dto.OlxProductDTO;
import org.payLate.dto.ProductDTO;
import org.payLate.requestModels.AddProductRequest;
import org.payLate.services.CSVService;
import org.payLate.services.OlxScraperService;
import org.payLate.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final OlxScraperService olxScraperService;
    private final CSVService csvService;

    @Autowired
    public ProductController(ProductService productService, CSVService csvService, OlxScraperService olxScraperService) {
        this.productService = productService;
        this.csvService = csvService;
        this.olxScraperService = olxScraperService;
    }

    @PostMapping("/{partner}/add")
    public ResponseEntity<?> addProduct(
            @PathVariable("partner") String partner,
            @RequestBody AddProductRequest addProductRequest) {
        try {
            productService.addProductForPartner(partner, addProductRequest);
            return ResponseEntity.ok("Product added successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to add product: " + e.getMessage());
        }
    }

    @GetMapping("/{partner}/{id}/exists")
    public ResponseEntity<Boolean> exists(
            @PathVariable("partner") String partner,
            @PathVariable("id") Long id) {
        boolean exists = false;
        switch (partner.toLowerCase()) {
            case "amazon" -> exists = productService.amazonProductExists(id);
            case "olx" -> exists = productService.olxProductExists(id);
            case "aliexpress" -> exists = productService.aliexpressProductExists(id);
            case "default" -> exists = productService.productExists(id);
        }
        return ResponseEntity.ok(exists);
    }

    // GET /api/products/{partner}/{id}
    @GetMapping("/{partner}/{id}")
    public ResponseEntity<?> getProductByPartnerAndId(
            @PathVariable("partner") String partner,
            @PathVariable("id") Long id) {
        switch (partner.toLowerCase()) {
            case "amazon" -> {
                Optional<AmazonProductDTO> productOpt = productService.getAmazonProductDTOById(id);
                return productOpt.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
            }
            case "olx" -> {
                Optional<OlxProductDTO> productOpt = productService.getOlxProductDTOById(id);
                return productOpt.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
            }
            case "aliexpress" -> {
                Optional<AliExpressProductDTO> productOpt = productService.getAliExpressProductDTOById(id);
                return productOpt.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
            }
            case "default" -> {
                Optional<ProductDTO> productOpt = productService.getProductDTOById(id);
                return productOpt.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
            }
            default -> {
                return ResponseEntity.badRequest().body("Unknown partner: " + partner);
            }
        }
    }
}