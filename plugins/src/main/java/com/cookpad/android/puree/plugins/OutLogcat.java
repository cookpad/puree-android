package com.cookpad.android.puree.plugins;

import android.util.Log;

import com.cookpad.android.puree.PureeOutput;

import org.json.JSONObject;

public class OutLogcat extends PureeOutput {
    private static final String TAG = OutLogcat.class.getSimpleName();
    public static final String TYPE = "logcat";

    public String type() {
        return TYPE;
    }

    @Override
    public Configuration configure(Configuration conf) {
        return conf;
    }

    @Override
    public void emit(JSONObject serializedLog) {
        Log.d(TAG, serializedLog.toString());
    }
}
