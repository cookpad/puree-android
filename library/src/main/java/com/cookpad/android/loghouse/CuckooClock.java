package com.cookpad.android.loghouse;

import android.os.Handler;

public class CuckooClock {
    private final String TAG = CuckooClock.class.getSimpleName();

    private Handler handler;
    private boolean hasAlreadySet;
    private Runnable callback;
    private int callMeAfter;

    public CuckooClock(final OnAlarmListener onAlarmListener, int callMeAfter) {
        handler = new Handler();
        this.callMeAfter = callMeAfter;
        hasAlreadySet = false;

        callback = new Runnable() {
            @Override
            public void run() {
                hasAlreadySet = false;
                onAlarmListener.onAlarm();
            }
        };
    }

    public interface OnAlarmListener {
        public void onAlarm();
    }

    public synchronized void setAlarm() {
        if (hasAlreadySet) {
            return;
        }

        handler.postDelayed(callback, callMeAfter);
        hasAlreadySet = true;
    }
}
