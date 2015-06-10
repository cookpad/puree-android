package com.cookpad.puree.async;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.runner.AndroidJUnit4;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class AsyncResultTest {

    @Test
    public void getWithSuccess() throws Exception {
        AsyncResult asyncResult = new AsyncResult();
        asyncResult.success();
        assertThat(asyncResult.get(), is(true));
    }

    @Test
    public void getWithFail() throws Exception {
        AsyncResult asyncResult = new AsyncResult();
        asyncResult.fail();
        assertThat(asyncResult.get(), is(false));
    }
}
