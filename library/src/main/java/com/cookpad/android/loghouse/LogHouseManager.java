package com.cookpad.android.loghouse;

import com.google.gson.Gson;

public class LogHouseManager {
    private static final String TAG = LogHouseManager.class.getSimpleName();
    private static final Gson GSON = new Gson();

    public static void ask(Log log) {
        String serializedLog = GSON.toJson(log);
        android.util.Log.d(TAG, serializedLog);
    }
}
