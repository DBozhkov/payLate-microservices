package org.payLate.strategy;

import org.payLate.dto.OlxProductDTO;
import org.payLate.requestModels.AddProductRequest;
import org.payLate.services.ProductService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OlxProductCreationStrategy implements ProductCreationStrategy {

    private final ProductService productService;

    public OlxProductCreationStrategy(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void createProduct(AddProductRequest request) {
        OlxProductDTO dto = new OlxProductDTO();
        dto.setProductName(request.getProductName());
        dto.setCategory(request.getCategory());
        dto.setPrice(request.getPrice());
        dto.setImgUrl(request.getImgUrl());
        dto.setDescription(request.getDescription());
        dto.setQuantity(request.getQuantity() != null ? request.getQuantity().intValue() : 1);
        dto.setRating(request.getRating());
        dto.setAuthorName(request.getAuthorName());
        dto.setAuthorUrl(request.getAuthorUrl());
        productService.saveOlxProducts(List.of(dto));
    }

    @Override
    public String getPartnerName() {
        return "olx";
    }
}