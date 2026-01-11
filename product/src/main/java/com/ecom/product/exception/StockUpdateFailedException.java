package com.ecom.product.exception;

public class StockUpdateFailedException extends RuntimeException{

    public StockUpdateFailedException(String message) {
        super(message);
    }
}
