package com.cookpad.android.loghouse.plugins;

import android.util.Log;

import com.cookpad.android.loghouse.LogHouseOutput;

import org.json.JSONObject;

import java.util.List;

public class OutLogcat extends LogHouseOutput {
    private static final String TAG = OutLogcat.class.getSimpleName();
    @Override
    public boolean emit(List<JSONObject> serializedLogs) {
        for (JSONObject serializedLog : serializedLogs) {
            Log.d(TAG, serializedLog.toString());
        }
        return true;
    }
}
