package com.cookpad.puree.retryable;

import android.os.Handler;

public class RetryableTaskRunner {
    private Handler handler;
    private boolean hasAlreadyStarted;
    private Runnable callback;
    private BuckoffCounter buckoffCounter;

    public RetryableTaskRunner(final Runnable task, int interval, int maxRetryCount) {
        this.buckoffCounter = new BuckoffCounter(interval, maxRetryCount);
        this.handler = new Handler();
        this.hasAlreadyStarted = false;

        this.callback = new Runnable() {
            @Override
            public void run() {
                task.run();
            }
        };
    }

    public synchronized void tryToStart() {
        if (hasAlreadyStarted) {
            return;
        }
        buckoffCounter.resetRetryCount();
        startDelayed();
    }

    private synchronized void startDelayed() {
        handler.removeCallbacks(callback);
        handler.postDelayed(callback, buckoffCounter.time());
        hasAlreadyStarted = true;
    }

    public synchronized void reset() {
        hasAlreadyStarted = false;
        buckoffCounter.resetRetryCount();
    }

    public synchronized void retryLater() {
        if (buckoffCounter.isRemainingRetryCount()) {
            buckoffCounter.incrementRetryCount();
            startDelayed();
        } else {
            reset();
        }
    }
}
