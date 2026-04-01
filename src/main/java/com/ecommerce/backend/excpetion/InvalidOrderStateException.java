package com.ecommerce.backend.excpetion;

import com.ecommerce.backend.entity.OrderStatus;
import lombok.Getter;

@Getter
public class InvalidOrderStateException extends RuntimeException {

    private final OrderStatus currentStatus;
    private final OrderStatus expectedStatus;

    public InvalidOrderStateException(OrderStatus currentStatus, OrderStatus expectedStatus) {
        super(String.format("Order is in '%s' status. Expected '%s' status to perform this action.",
                currentStatus, expectedStatus));
        this.currentStatus = currentStatus;
        this.expectedStatus = expectedStatus;
    }

    // For cases where any specific expected status doesn't apply
    public InvalidOrderStateException(String message) {
        super(message);
        this.currentStatus = null;
        this.expectedStatus = null;
    }
}
