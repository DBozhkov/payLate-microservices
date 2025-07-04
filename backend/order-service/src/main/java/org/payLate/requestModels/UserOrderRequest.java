package org.payLate.requestModels;

import lombok.Data;

import java.util.List;

@Data
public class UserOrderRequest {

    private String userEmail;

    private List<OrderItemRequest> items;
}