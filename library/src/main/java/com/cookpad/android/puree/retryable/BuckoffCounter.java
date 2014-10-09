package com.cookpad.android.puree.retryable;

public class BuckoffCounter {
    private int baseTime;
    private int retryCount;

    public BuckoffCounter(int baseTime) {
        this.baseTime = baseTime;
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
