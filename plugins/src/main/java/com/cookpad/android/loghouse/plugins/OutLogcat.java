package com.cookpad.android.loghouse.plugins;

import android.util.Log;

import com.cookpad.android.loghouse.LogHouse;

import org.json.JSONObject;

public class OutLogcat extends LogHouse.Output {
    private static final String TAG = OutLogcat.class.getSimpleName();
    public static final String TYPE = "logcat";

    public String type() {
        return TYPE;
    }

    @Override
    public void emit(JSONObject serializedLog) {
        Log.d(TAG, serializedLog.toString());
    }
}
