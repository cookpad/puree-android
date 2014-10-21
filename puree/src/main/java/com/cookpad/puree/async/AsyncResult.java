package com.cookpad.puree.async;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AsyncResult extends CountDownLatch {
    private boolean result = false;

    public AsyncResult() {
        this(1);
    }

    public AsyncResult(int count) {
        super(count);
    }

    public boolean get() throws InterruptedException {
        await(1, TimeUnit.MINUTES);
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
