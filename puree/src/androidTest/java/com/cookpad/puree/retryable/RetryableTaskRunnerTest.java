package com.cookpad.puree.retryable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.os.Handler;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

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
    public void ensureToCallMeAfterSetTime() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        RetryableTaskRunner task = new RetryableTaskRunner(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 10, 5, handler);

        task.tryToStart();

        assertThat(latch.await(30, TimeUnit.MILLISECONDS), is(true));
    }
}
