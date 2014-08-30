package com.cookpad.android.loghouse;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class CuckooClock extends BroadcastReceiver {
    private static final String TAG = CuckooClock.class.getSimpleName();

    private static OnAlarmListener onAlarmListener;
    private static int callMeAfter;
    private static int timeUnit;

    public static void setup(OnAlarmListener onAlarmListener, int callMeAfter, int timeUnit) {
        CuckooClock.onAlarmListener = onAlarmListener;
        CuckooClock.callMeAfter = callMeAfter;
        CuckooClock.timeUnit = timeUnit;
    }

    public interface OnAlarmListener {
        public void onAlarm();
    }

    public static void setAlarm(Context context) {
        Intent intent = new Intent(context, CuckooClock.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(timeUnit, callMeAfter);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        android.util.Log.d(TAG, "onReceive is called");
        onAlarmListener.onAlarm();
    }
}
