package com.cookpad.android.loghouse.exceptions;

public class DeliveryPersonUndefinedException extends RuntimeException {

    public DeliveryPersonUndefinedException() {
        super();
    }

    public DeliveryPersonUndefinedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DeliveryPersonUndefinedException(String detailMessage) {
        super(detailMessage);
    }

    public DeliveryPersonUndefinedException(Throwable throwable) {
        super(throwable);
    }
}

