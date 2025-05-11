package org.payLate.controllers;

import org.payLate.services.CSVService;
import org.payLate.services.OlxScraperService;
import org.payLate.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("https://localhost:3000")
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

}
