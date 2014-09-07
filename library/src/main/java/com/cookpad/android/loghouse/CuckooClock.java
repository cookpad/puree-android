package com.cookpad.android.loghouse;

import android.os.Handler;

public class CuckooClock {
    private final String TAG = CuckooClock.class.getSimpleName();

    private Handler handler;
    private boolean hasAlreadySet;
    private Runnable callback;
    private int callMeAfter;
    private int retryCount = 1;
    private int buckOffTime;

    public CuckooClock(final OnAlarmListener onAlarmListener, int callMeAfter) {
        handler = new Handler();
        this.callMeAfter = callMeAfter;
        hasAlreadySet = false;

        callback = new Runnable() {
            @Override
            public void run() {
                hasAlreadySet = false;
                retryCount = 1;
                onAlarmListener.onAlarm();
            }
        };
    }

    public interface OnAlarmListener {
        public void onAlarm();
    }

    public synchronized void setAlarm() {
        if (hasAlreadySet && retryCount == 1) {
            return;
        }
        retryCount = 1;
        forceSetAlarm();
    }

    private synchronized void forceSetAlarm() {
        buckOffTime = callMeAfter * retryCount;
        handler.removeCallbacks(callback);
        handler.postDelayed(callback, buckOffTime);
        hasAlreadySet = true;
    }

    public synchronized void retryLater() {
        retryCount++;
        forceSetAlarm();
    }
}
