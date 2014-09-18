package com.cookpad.android.loghouse.lazy;

import android.os.Handler;

public class LazyTaskRunner {
    private Handler handler;
    private boolean hasAlreadySet;
    private Runnable callback;
    private int interval;
    private int retryCount = 1;

    public LazyTaskRunner(final LazyTask task, final int interval) {
        this.interval = interval;
        this.handler = new Handler();
        this.hasAlreadySet = false;

        this.callback = new Runnable() {
            @Override
            public void run() {
                hasAlreadySet = false;
                retryCount = 1;
                task.run();
            }
        };
    }

    public synchronized void tryToStart() {
        if (hasAlreadySet && retryCount == 1) {
            return;
        }
        retryCount = 1;
        startDelayed();
    }

    private synchronized void startDelayed() {
        handler.removeCallbacks(callback);

        int buckOffTime = interval * retryCount;
        handler.postDelayed(callback, buckOffTime);

        hasAlreadySet = true;
    }

    public synchronized void retryLater() {
        retryCount++;
        startDelayed();
    }
}
