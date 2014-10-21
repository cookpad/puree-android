package com.cookpad.android.puree.async;

import android.test.AndroidTestCase;

import com.cookpad.android.puree.async.AsyncResult;

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
