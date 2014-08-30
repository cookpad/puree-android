package com.cookpad.android.loghouse;

import android.test.AndroidTestCase;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CuckooClockTest extends AndroidTestCase {

    public void testEnsureToCallMeAfterSetTime() {
        final CountDownLatch latch = new CountDownLatch(1);

        CuckooClock.setup(new CuckooClock.OnAlarmListener() {
            @Override
            public void onAlarm() {
                latch.countDown();
            }
        }, 1, Calendar.SECOND);

        try {
            latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            fail();
        }
    }
}
