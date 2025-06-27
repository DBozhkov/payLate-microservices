package org.payLate.dto;

import lombok.Data;

@Data
public class AmazonProductDTO {

    private String productName;

    private String category;

    private Double price;

    private String imgUrl;

    private String description;

    private Integer quantity;

    private Integer availableProducts;

    private Double rating;

    private String authorName;

    private String authorUrl;

}
