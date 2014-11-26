package com.cookpad.puree.retryable;

import android.os.Handler;

public class RetryableTaskRunner {
    private Handler handler;
    private boolean hasAlreadyStarted;
    private Runnable callback;
    private BackoffCounter backoffCounter;

    public RetryableTaskRunner(final Runnable task, int intervalMillis, int maxRetryCount) {
        this.backoffCounter = new BackoffCounter(intervalMillis, maxRetryCount);
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
        backoffCounter.resetRetryCount();
        startDelayed();
    }

    private synchronized void startDelayed() {
        handler.removeCallbacks(callback);
        handler.postDelayed(callback, backoffCounter.time());
        hasAlreadyStarted = true;
    }

    public synchronized void reset() {
        hasAlreadyStarted = false;
        backoffCounter.resetRetryCount();
    }

    public synchronized void retryLater() {
        if (backoffCounter.isRemainingRetryCount()) {
            backoffCounter.incrementRetryCount();
            startDelayed();
        } else {
            reset();
        }
    }
}
