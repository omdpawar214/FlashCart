package com.FlashCart.FlashSaleSystem.ErrorControl;

public class APIException extends RuntimeException {
    String msg;
    public APIException(String message) {
        this.msg=message;
    }
}
