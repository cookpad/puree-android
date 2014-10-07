package com.cookpad.android.loghouse.exceptions;

public class LogHouseNotInitializedException extends RuntimeException {
    public LogHouseNotInitializedException() {
        super();
    }

    public LogHouseNotInitializedException(String detailMessage) {
        super(detailMessage);
    }

    public LogHouseNotInitializedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public LogHouseNotInitializedException(Throwable throwable) {
        super(throwable);
    }
}
