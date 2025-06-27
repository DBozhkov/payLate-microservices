package org.payLate.dto;

import lombok.Data;

@Data
public class AliExpressProductDTO {

    private String productName;

    private String category;

    private Double price;

    private String imgUrl;

    private String description;

    private Integer quantity;

    private Double rating;

    private String authorName;

    private String authorUrl;
}
