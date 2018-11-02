package com.cookpad.puree.retryable;

import com.cookpad.puree.internal.BackoffCounter;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class BackoffCounterTest {
    @Test
    public void time() {
        BackoffCounter backoffCounter = new BackoffCounter(10, 3);

        assertThat(backoffCounter.getRetryCount(), is(0));
        assertThat(backoffCounter.timeInMillis(), is(10L));
        assertThat(backoffCounter.isRemainingRetryCount(), is(true));

        backoffCounter.incrementRetryCount();
        assertThat(backoffCounter.getRetryCount(), is(1));
        assertThat(backoffCounter.timeInMillis(), is(20L));
        assertThat(backoffCounter.isRemainingRetryCount(), is(true));

        backoffCounter.incrementRetryCount();
        assertThat(backoffCounter.getRetryCount(), is(2));
        assertThat(backoffCounter.timeInMillis(), is(30L));
        assertThat(backoffCounter.isRemainingRetryCount(), is(true));

        backoffCounter.incrementRetryCount();
        assertThat(backoffCounter.getRetryCount(), is(3));
        assertThat(backoffCounter.timeInMillis(), is(40L));
        assertThat(backoffCounter.isRemainingRetryCount(), is(false));

        backoffCounter.resetRetryCount();
        assertThat(backoffCounter.getRetryCount(), is(0));
        assertThat(backoffCounter.timeInMillis(), is(10L));
        assertThat(backoffCounter.isRemainingRetryCount(), is(true));
    }
}
