package org.payLate.strategy;

import org.payLate.requestModels.AddProductRequest;

public interface ProductCreationStrategy {
    void createProduct(AddProductRequest request);
    String getPartnerName();
}