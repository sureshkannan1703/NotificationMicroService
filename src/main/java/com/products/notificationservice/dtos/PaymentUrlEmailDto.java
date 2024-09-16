package com.products.notificationservice.dtos;

import lombok.Data;

@Data
public class PaymentUrlEmailDto {

    long orderId;

    double amount;

    String email;

    String paymentUrl;
}
