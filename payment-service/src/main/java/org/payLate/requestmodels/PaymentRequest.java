package org.payLate.requestmodels;

import lombok.Data;

@Data
public class PaymentRequest {

    private int amount;
    private String currency;
    private String receiptEmail;
}
