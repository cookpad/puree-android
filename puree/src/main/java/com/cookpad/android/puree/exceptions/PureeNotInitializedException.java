package com.cookpad.android.puree.exceptions;

public class PureeNotInitializedException extends RuntimeException {
    public PureeNotInitializedException() {
        super();
    }

    public PureeNotInitializedException(String detailMessage) {
        super(detailMessage);
    }

    public PureeNotInitializedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public PureeNotInitializedException(Throwable throwable) {
        super(throwable);
    }
}
