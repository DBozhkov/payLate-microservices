package org.payLate.controllers;


import org.json.JSONArray;
import org.payLate.dto.AliExpressProductDTO;
import org.payLate.dto.AmazonProductDTO;
import org.payLate.dto.OlxProductDTO;
import org.payLate.services.CSVService;
import org.payLate.services.OlxScraperService;
import org.payLate.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products/csv")
public class CSVController {

    private ProductService productService;

    private OlxScraperService olxScraperService;

    private CSVService csvService;

    @Autowired
    public CSVController(CSVService csvService, ProductService productService) {
        this.csvService = csvService;
        this.productService = productService;
    }

    @PostMapping("/importOlxProducts")
    public ResponseEntity<String> importOlxProducts() {
        productService.saveOlxProducts(csvService.readOlxCsvFile());
        return ResponseEntity.ok("OLX products imported successfully");
    }

    @PostMapping("/importAliExpressProducts")
    public ResponseEntity<String> importAliExpressProducts() {
        productService.saveAliexpressProducts(csvService.readAliexpressCsvFile());
        return ResponseEntity.ok("AliExpress products imported successfully");
    }

    @PostMapping("/importAmazonProducts")
    public ResponseEntity<String> importAmazonProducts() {
        productService.saveAmazonProducts(csvService.readAmazonCsvFile());
        return ResponseEntity.ok("Amazon products imported successfully");
    }


    @GetMapping("/saveOlxProducts")
    public ResponseEntity<String> scrapeAndSaveProducts() {
        olxScraperService.scrapeAndSaveToCsv();
        return ResponseEntity.ok("Products scraped and saved to CSV.");
    }

    @GetMapping("/aliProducts")
    public String getCsvDataAli() {
        JSONArray jsonData = csvService.getCsvDataAsJsonAli();
        return jsonData.toString(2);
    }

    @GetMapping("/olxProducts")
    public String getCsvDataOlx() {
        JSONArray jsonData = csvService.getCsvDataAsJsonOlx();
        return jsonData.toString(2);
    }

    @GetMapping("/amazonProducts")
    public String getCsvDataAmazon() {
        JSONArray jsonData = csvService.getCsvDataAsJsonAmazon();
        return jsonData.toString(2);
    }

    @GetMapping("/olxProductsList")
    public ResponseEntity<List<OlxProductDTO>> getOlxProducts() {
        List<OlxProductDTO> olxProducts = csvService.readOlxCsvFile();
        return ResponseEntity.ok(olxProducts);
    }

    @GetMapping("/amazonProductsList")
    public ResponseEntity<List<AmazonProductDTO>> getAmazonProducts() {
        List<AmazonProductDTO> amazonProducts = csvService.readAmazonCsvFile();
        return ResponseEntity.ok(amazonProducts);
    }

    @GetMapping("/aliExpressProductsList")
    public ResponseEntity<List<AliExpressProductDTO>> getAliExpressProducts() {
        List<AliExpressProductDTO> aliExpressProducts = csvService.readAliexpressCsvFile();
        return ResponseEntity.ok(aliExpressProducts);
    }
}
