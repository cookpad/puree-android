package com.cookpad.puree.retryable;

import com.cookpad.puree.internal.RetryableTaskRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.runner.AndroidJUnit4;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class RetryableTaskRunnerTest {

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    @Test
    public void testEnsureToCallMeAfterSetTime() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        RetryableTaskRunner task = new RetryableTaskRunner(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 10, 5, executor);

        task.tryToStart();

        assertThat(latch.await(20, TimeUnit.MILLISECONDS), is(true));
    }

    @Test
    public void testRetryLater() throws Exception {
        final CountDownLatch latch = new CountDownLatch(2);

        RetryableTaskRunner task = new RetryableTaskRunner(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
            }
        }, 10, 5, executor);

        task.tryToStart();

        assertThat(latch.await(20, TimeUnit.MILLISECONDS), is(false));

        task.retryLater();

        assertThat(latch.await(40, TimeUnit.MILLISECONDS), is(true));

        assertThat(latch.getCount(), is(0L));
    }
}
