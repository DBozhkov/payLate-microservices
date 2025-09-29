package org.payLate.strategy;

import org.payLate.dto.AmazonProductDTO;
import org.payLate.requestModels.AddProductRequest;
import org.payLate.services.ProductService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AmazonProductCreationStrategy implements ProductCreationStrategy {

    private final ProductService productService;

    public AmazonProductCreationStrategy(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void createProduct(AddProductRequest request) {
        AmazonProductDTO dto = new AmazonProductDTO();
        dto.setProductName(request.getProductName());
        dto.setCategory(request.getCategory());
        dto.setPrice(request.getPrice());
        dto.setImgUrl(request.getImgUrl());
        dto.setDescription(request.getDescription());
        dto.setQuantity(request.getQuantity() != null ? request.getQuantity().intValue() : 1);
        dto.setRating(request.getRating());
        dto.setAuthorName(request.getAuthorName());
        dto.setAuthorUrl(request.getAuthorUrl());
        productService.saveAmazonProducts(List.of(dto));
    }

    @Override
    public String getPartnerName() {
        return "amazon";
    }
}