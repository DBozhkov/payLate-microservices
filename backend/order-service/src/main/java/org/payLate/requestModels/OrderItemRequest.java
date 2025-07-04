package org.payLate.requestModels;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long productId;
    private int quantity;
    private String partner;
    private String imgUrl;
    private double price;
}