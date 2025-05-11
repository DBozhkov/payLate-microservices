package org.payLate.requestModels;

import lombok.Data;

@Data
public class AddProductRequest {

    private String productName;

    private Double price;

    private String description;

    private String category;

    private Long quantity;

    private String imgUrl;

    private Double rating;

    private String authorName;

    private String authorUrl;

    private String productType;
}
