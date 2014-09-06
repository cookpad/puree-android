package com.cookpad.android.loghouse;

import android.test.AndroidTestCase;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CuckooClockTest extends AndroidTestCase {

    public void testEnsureToCallMeAfterSetTime() {
        final CountDownLatch latch = new CountDownLatch(1);

        CuckooClock.OnAlarmListener onAlarmListener = new CuckooClock.OnAlarmListener() {
            @Override
            public void onAlarm() {
                latch.countDown();
            }
        };

        CuckooClock cuckooClock = new CuckooClock(onAlarmListener, 10);
        cuckooClock.setAlarm();

        try {
            latch.await(30, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            fail();
        }
    }
}
