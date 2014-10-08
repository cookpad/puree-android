package com.cookpad.android.puree.lazy;

import android.os.Handler;

public class LazyTaskRunner {
    private Handler handler;
    private boolean hasAlreadySet;
    private Runnable callback;
    private int interval;
    private int retryCount;

    public LazyTaskRunner(final LazyTask task, final int interval) {
        this.interval = interval;
        this.handler = new Handler();
        this.hasAlreadySet = false;

        this.callback = new Runnable() {
            @Override
            public void run() {
                task.run();
            }
        };
    }

    public synchronized void reset() {
        hasAlreadySet = false;
        retryCount = 0;
    }

    public synchronized void tryToStart() {
        if (hasAlreadySet && retryCount == 0) {
            return;
        }
        retryCount = 0;
        startDelayed();
    }

    private synchronized void startDelayed() {
        handler.removeCallbacks(callback);

        int buckOffTime = interval * retryCount;
        handler.postDelayed(callback, interval + buckOffTime);

        hasAlreadySet = true;
    }

    public synchronized void retryLater() {
        retryCount++;
        startDelayed();
    }
}
