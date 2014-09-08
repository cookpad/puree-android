package com.cookpad.android.loghouse.async;

import android.test.AndroidTestCase;

public class AsyncResultTest extends AndroidTestCase {
    public void testGet() throws InterruptedException {
        {
            AsyncResult asyncResult = new AsyncResult();
            asyncResult.success();
            assertTrue(asyncResult.get());
        }
        {
            AsyncResult asyncResult = new AsyncResult();
            asyncResult.fail();
            assertFalse(asyncResult.get());
        }
    }
}
