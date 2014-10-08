package com.cookpad.android.puree;

import android.test.AndroidTestCase;

import com.cookpad.android.puree.lazy.LazyTask;
import com.cookpad.android.puree.lazy.LazyTaskRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LazyTaskRunnerTest extends AndroidTestCase {

    public void testEnsureToCallMeAfterSetTime() {
        final CountDownLatch latch = new CountDownLatch(1);

        LazyTaskRunner lazyTaskRunner = new LazyTaskRunner(new LazyTask() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 10);

        lazyTaskRunner.tryToStart();

        try {
            latch.await(30, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            fail();
        }
    }
}
