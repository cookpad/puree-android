package com.cookpad.puree.internal;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RetryableTaskRunner {

    private boolean hasAlreadyStarted;

    private Runnable task;

    private BackoffCounter backoffCounter;

    private ScheduledExecutorService executor;

    private ScheduledFuture<?> future;

    public RetryableTaskRunner(final Runnable task, int intervalMillis, int maxRetryCount, ScheduledExecutorService executor) {
        this.backoffCounter = new BackoffCounter(intervalMillis, maxRetryCount);
        this.executor = executor;
        this.hasAlreadyStarted = false;

        this.task = task;
    }

    public synchronized void tryToStart() {
        if (hasAlreadyStarted) {
            return;
        }
        backoffCounter.resetRetryCount();
        startDelayed();
    }

    private synchronized void startDelayed() {
        future = executor.schedule(task, backoffCounter.timeInMillis(), TimeUnit.MILLISECONDS);
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
