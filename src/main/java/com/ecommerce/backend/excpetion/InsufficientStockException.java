package com.ecommerce.backend.excpetion;

import lombok.Getter;

@Getter
public class InsufficientStockException extends RuntimeException {

    private final String productName;
    private final Integer availableStock;
    private final Integer requestedQuantity;

    public InsufficientStockException(String productName,
                                      Integer availableStock,
                                      Integer requestedQuantity) {
        super(String.format(
                "Insufficient stock for product '%s'. Available: %d, Requested: %d",
                productName, availableStock, requestedQuantity));
        this.productName = productName;
        this.availableStock = availableStock;
        this.requestedQuantity = requestedQuantity;
    }
}