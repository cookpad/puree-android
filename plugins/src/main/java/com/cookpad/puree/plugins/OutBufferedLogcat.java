package com.cookpad.puree.plugins;

import android.util.Log;

import com.cookpad.puree.OutputConfiguration;
import com.cookpad.puree.outputs.PureeBufferedOutput;
import com.cookpad.puree.async.AsyncResult;

import org.json.JSONArray;

public class OutBufferedLogcat extends PureeBufferedOutput {
    public static final String TYPE = "buffered_logcat";

    private static final String TAG = OutBufferedLogcat.class.getSimpleName();

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public OutputConfiguration configure(OutputConfiguration conf) {
        conf.setFlushInterval(2000);
        conf.setLogsPerRequest(3);
        return conf;
    }

    @Override
    public void emit(JSONArray serializedLogs, AsyncResult asyncResult) {
        Log.d(TAG, serializedLogs.toString());
        asyncResult.success();
    }
}
