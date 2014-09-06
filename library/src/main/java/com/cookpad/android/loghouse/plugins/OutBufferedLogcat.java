package com.cookpad.android.loghouse.plugins;

import android.util.Log;

import com.cookpad.android.loghouse.LogHouse;

import org.json.JSONObject;

import java.util.List;

public class OutBufferedLogcat extends LogHouse.BufferedOutput {
    private static final String TAG = OutBufferedLogcat.class.getSimpleName();

    @Override
    public boolean emit(List<JSONObject> serializedLogs) {
        for (JSONObject serializedLog : serializedLogs) {
            Log.d(TAG, serializedLog.toString());
        }
        return true;
    }
}
