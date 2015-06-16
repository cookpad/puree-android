package com.cookpad.puree.retryable;

import com.cookpad.puree.internal.BackoffCounter;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.runner.AndroidJUnit4;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class BackoffCounterTest {
    @Test
    public void time() {
        BackoffCounter backoffCounter = new BackoffCounter(10, 3);

        assertThat(backoffCounter.getRetryCount(), is(0));
        assertThat(backoffCounter.time(), is(10));
        assertThat(backoffCounter.isRemainingRetryCount(), is(true));

        backoffCounter.incrementRetryCount();
        assertThat(backoffCounter.getRetryCount(), is(1));
        assertThat(backoffCounter.time(), is(20));
        assertThat(backoffCounter.isRemainingRetryCount(), is(true));

        backoffCounter.incrementRetryCount();
        assertThat(backoffCounter.getRetryCount(), is(2));
        assertThat(backoffCounter.time(), is(30));
        assertThat(backoffCounter.isRemainingRetryCount(), is(true));

        backoffCounter.incrementRetryCount();
        assertThat(backoffCounter.getRetryCount(), is(3));
        assertThat(backoffCounter.time(), is(40));
        assertThat(backoffCounter.isRemainingRetryCount(), is(false));

        backoffCounter.resetRetryCount();
        assertThat(backoffCounter.getRetryCount(), is(0));
        assertThat(backoffCounter.time(), is(10));
        assertThat(backoffCounter.isRemainingRetryCount(), is(true));
    }
}
