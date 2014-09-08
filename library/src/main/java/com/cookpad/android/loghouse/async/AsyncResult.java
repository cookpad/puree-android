package com.cookpad.android.loghouse.async;

import java.util.concurrent.CountDownLatch;

public class AsyncResult extends CountDownLatch {
    private boolean result = false;

    public AsyncResult() {
        this(1);
    }

    public AsyncResult(int count) {
        super(count);
    }

    public boolean get() throws InterruptedException {
        await();
        return result;
    }

    public void success() {
        result = true;
        countDown();
    }

    public void fail() {
        result = false;
        countDown();
    }
}
