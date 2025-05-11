package org.payLate.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserOrderRequest {

    private String userEmail;

    private List<OrderItemRequest> items;
}