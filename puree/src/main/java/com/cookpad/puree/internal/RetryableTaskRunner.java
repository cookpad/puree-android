package com.cookpad.puree.internal;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RetryableTaskRunner {

    private Runnable task;

    private BackoffCounter backoffCounter;

    private ScheduledExecutorService executor;

    private ScheduledFuture<?> future;

    public RetryableTaskRunner(final Runnable task, int intervalMillis, int maxRetryCount, ScheduledExecutorService executor) {
        this.backoffCounter = new BackoffCounter(intervalMillis, maxRetryCount);
        this.executor = executor;
        this.task = task;
        this.future = null;
    }

    public synchronized void tryToStart() {
        if (future != null) {
            return;
        }
        backoffCounter.resetRetryCount();
        startDelayed();
    }

    private void startDelayed() {
        if (future != null) {
            future.cancel(false);
        }
        future = executor.scheduleAtFixedRate(task, backoffCounter.timeInMillis(), backoffCounter.timeInMillis(), TimeUnit.MILLISECONDS);
    }

    public synchronized void reset() {
        future = null;
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
