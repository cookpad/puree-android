package com.cookpad.android.loghouse.plugins;

import android.util.Log;

import com.cookpad.android.loghouse.LogHouse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class OutBufferedLogcat extends LogHouse.BufferedOutput {
    private static final String TAG = OutBufferedLogcat.class.getSimpleName();
    public static final String TYPE = "buffered_logcat";

    public String type() {
        return TYPE;
    }

    @Override
    protected int callMeAfter() {
        return 2000;
    }

    @Override
    public boolean emit(List<JSONObject> serializedLogs) {
        JSONArray log = new JSONArray();
        for (JSONObject serializedLog : serializedLogs) {
            log.put(serializedLog);
        }
        Log.d(TAG, log.toString());
        return true;
    }
}
