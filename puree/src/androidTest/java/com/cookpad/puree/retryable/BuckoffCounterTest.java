package com.cookpad.puree.retryable;

import android.test.AndroidTestCase;

public class BuckoffCounterTest extends AndroidTestCase {
    public void testTime() {
        BuckoffCounter buckoffCounter = new BuckoffCounter(10, 3);

        assertEquals(0, buckoffCounter.getRetryCount());
        assertEquals(10, buckoffCounter.time());
        assertTrue(buckoffCounter.isRemainingRetryCount());

        buckoffCounter.incrementRetryCount();
        assertEquals(1, buckoffCounter.getRetryCount());
        assertEquals(20, buckoffCounter.time());
        assertTrue(buckoffCounter.isRemainingRetryCount());

        buckoffCounter.incrementRetryCount();
        assertEquals(2, buckoffCounter.getRetryCount());
        assertEquals(30, buckoffCounter.time());
        assertTrue(buckoffCounter.isRemainingRetryCount());

        buckoffCounter.incrementRetryCount();
        assertEquals(3, buckoffCounter.getRetryCount());
        assertEquals(40, buckoffCounter.time());
        assertFalse(buckoffCounter.isRemainingRetryCount());

        buckoffCounter.resetRetryCount();
        assertEquals(0, buckoffCounter.getRetryCount());
        assertEquals(10, buckoffCounter.time());
        assertTrue(buckoffCounter.isRemainingRetryCount());
    }
}
