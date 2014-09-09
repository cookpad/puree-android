package com.cookpad.android.loghouse.plugins;

import android.util.Log;

import com.cookpad.android.loghouse.LogHouseBufferedOutput;
import com.cookpad.android.loghouse.LogHouseOutput;
import com.cookpad.android.loghouse.async.AsyncResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class OutBufferedLogcat extends LogHouseBufferedOutput {
    private static final String TAG = OutBufferedLogcat.class.getSimpleName();
    public static final String TYPE = "buffered_logcat";

    public String type() {
        return TYPE;
    }

    @Override
    public Configuration configure(Configuration conf) {
        conf.setFlushInterval(2000);
        conf.setLogsPerRequest(3);
        return conf;
    }

    @Override
    public void emit(List<JSONObject> serializedLogs, AsyncResult asyncResult) {
        JSONArray log = new JSONArray();
        for (JSONObject serializedLog : serializedLogs) {
            log.put(serializedLog);
        }
        Log.d(TAG, log.toString());

        asyncResult.success();
    }
}
