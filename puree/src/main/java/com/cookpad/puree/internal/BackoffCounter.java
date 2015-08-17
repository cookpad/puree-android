package com.cookpad.puree.internal;

public class BackoffCounter {

    private final int baseTimeMillis;

    private final int maxRetryCount;

    private int retryCount = 0;

    public int getRetryCount() {
        return retryCount;
    }

    public BackoffCounter(int baseTimeMillis, int maxRetryCount) {
        this.baseTimeMillis = baseTimeMillis;
        this.maxRetryCount = maxRetryCount;
    }

    public boolean isRemainingRetryCount() {
        return ((maxRetryCount - retryCount) > 0);
    }

    public void incrementRetryCount() {
        retryCount++;
    }

    public void resetRetryCount() {
        retryCount = 0;
    }

    public long timeInMillis() {
        if (retryCount == 0) {
            return baseTimeMillis;
        } else {
            return baseTimeMillis * (retryCount + 1);
        }
    }
}
