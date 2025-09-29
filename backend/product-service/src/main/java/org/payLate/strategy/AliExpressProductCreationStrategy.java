package org.payLate.strategy;

import org.payLate.dto.AliExpressProductDTO;
import org.payLate.requestModels.AddProductRequest;
import org.payLate.services.ProductService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AliExpressProductCreationStrategy implements ProductCreationStrategy {

    private final ProductService productService;

    public AliExpressProductCreationStrategy(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void createProduct(AddProductRequest request) {
        AliExpressProductDTO dto = new AliExpressProductDTO();
        dto.setProductName(request.getProductName());
        dto.setCategory(request.getCategory());
        dto.setPrice(request.getPrice());
        dto.setImgUrl(request.getImgUrl());
        dto.setDescription(request.getDescription());
        dto.setQuantity(request.getQuantity() != null ? request.getQuantity().intValue() : 1);
        dto.setRating(request.getRating());
        dto.setAuthorName(request.getAuthorName());
        dto.setAuthorUrl(request.getAuthorUrl());
        productService.saveAliexpressProducts(List.of(dto));
    }

    @Override
    public String getPartnerName() {
        return "aliexpress";
    }
}