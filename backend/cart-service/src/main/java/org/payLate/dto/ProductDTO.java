package org.payLate.dto;

import lombok.Data;

@Data
public class ProductDTO {

    private Long id;

    private String productName;

    private Double price;

    private String description;

    private String category;

    private int quantity;

    private String imgUrl;

    private Double rating;

    private String authorName;

    private String partner;
}