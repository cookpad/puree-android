package com.cookpad.puree.retryable;

import android.test.AndroidTestCase;

public class BackoffCounterTest extends AndroidTestCase {
    public void testTime() {
        BackoffCounter backoffCounter = new BackoffCounter(10, 3);

        assertEquals(0, backoffCounter.getRetryCount());
        assertEquals(10, backoffCounter.time());
        assertTrue(backoffCounter.isRemainingRetryCount());

        backoffCounter.incrementRetryCount();
        assertEquals(1, backoffCounter.getRetryCount());
        assertEquals(20, backoffCounter.time());
        assertTrue(backoffCounter.isRemainingRetryCount());

        backoffCounter.incrementRetryCount();
        assertEquals(2, backoffCounter.getRetryCount());
        assertEquals(30, backoffCounter.time());
        assertTrue(backoffCounter.isRemainingRetryCount());

        backoffCounter.incrementRetryCount();
        assertEquals(3, backoffCounter.getRetryCount());
        assertEquals(40, backoffCounter.time());
        assertFalse(backoffCounter.isRemainingRetryCount());

        backoffCounter.resetRetryCount();
        assertEquals(0, backoffCounter.getRetryCount());
        assertEquals(10, backoffCounter.time());
        assertTrue(backoffCounter.isRemainingRetryCount());
    }
}
