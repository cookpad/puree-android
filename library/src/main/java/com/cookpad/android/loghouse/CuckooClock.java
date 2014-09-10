package com.cookpad.android.loghouse;

import android.os.Handler;

public class CuckooClock {
    private Handler handler;
    private boolean hasAlreadySet;
    private Runnable callback;
    private int interval;
    private int retryCount = 1;

    public CuckooClock(final OnAlarmListener onAlarmListener, int interval) {
        handler = new Handler();
        this.interval = interval;
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
        handler.removeCallbacks(callback);

        int buckOffTime = interval * retryCount;
        handler.postDelayed(callback, buckOffTime);

        hasAlreadySet = true;
    }

    public synchronized void retryLater() {
        retryCount++;
        forceSetAlarm();
    }
}
