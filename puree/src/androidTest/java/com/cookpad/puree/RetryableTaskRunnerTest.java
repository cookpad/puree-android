package com.cookpad.puree;

import android.os.Handler;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cookpad.puree.retryable.RetryableTaskRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class RetryableTaskRunnerTest {
    private Handler handler;

    @Before
    public void setUp() throws Exception {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                handler = new Handler();
            }
        });
    }

    @Test
    public void ensureToCallMeAfterSetTime() {
        final CountDownLatch latch = new CountDownLatch(1);

        RetryableTaskRunner retryableTaskRunner = new RetryableTaskRunner(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 10, 5, handler);

        retryableTaskRunner.tryToStart();

        try {
            latch.await(30, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
    }
}
