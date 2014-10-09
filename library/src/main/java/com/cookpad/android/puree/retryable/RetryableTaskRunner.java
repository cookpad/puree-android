package com.cookpad.android.puree.retryable;

import android.os.Handler;

public class RetryableTaskRunner {
    private Handler handler;
    private boolean hasAlreadySet;
    private Runnable callback;
    private BuckoffCounter buckoffCounter;

    public RetryableTaskRunner(final Runnable task, final int interval) {
        this.buckoffCounter = new BuckoffCounter(interval);
        this.handler = new Handler();
        this.hasAlreadySet = false;

        this.callback = new Runnable() {
            @Override
            public void run() {
                task.run();
            }
        };
    }

    public synchronized void tryToStart() {
        if (hasAlreadySet) {
            return;
        }
        buckoffCounter.resetRetryCount();
        startDelayed();
    }

    private synchronized void startDelayed() {
        handler.removeCallbacks(callback);
        handler.postDelayed(callback, buckoffCounter.time());
        hasAlreadySet = true;
    }

    public synchronized void reset() {
        hasAlreadySet = false;
        buckoffCounter.resetRetryCount();
    }

    public synchronized void retryLater() {
        buckoffCounter.incrementRetryCount();
        startDelayed();
    }
}
