package com.cookpad.puree.async;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class AsyncResultTest {
    @Test
    public void get() throws InterruptedException {
        {
            AsyncResult asyncResult = new AsyncResult();
            asyncResult.success();
            assertThat(asyncResult.get(), is(true));
        }
        {
            AsyncResult asyncResult = new AsyncResult();
            asyncResult.fail();
            assertThat(asyncResult.get(), is(false));
        }
    }
}
