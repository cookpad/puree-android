package com.cookpad.puree.retryable;

public class BuckoffCounter {
    private final int baseTime;
    private final int maxRetryCount;
    private int retryCount = 0;

    public int getRetryCount() {
        return retryCount;
    }

    public BuckoffCounter(int baseTime, int maxRetryCount) {
        this.baseTime = baseTime;
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

    public int time() {
        if (retryCount == 0) {
            return baseTime;
        } else {
            return baseTime * (retryCount + 1);
        }
    }
}
