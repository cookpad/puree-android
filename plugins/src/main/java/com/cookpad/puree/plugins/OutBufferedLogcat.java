package com.cookpad.puree.plugins;


import com.cookpad.puree.async.AsyncResult;
import com.cookpad.puree.outputs.OutputConfiguration;
import com.cookpad.puree.outputs.PureeBufferedOutput;

import org.json.JSONArray;

import android.util.Log;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;


@ParametersAreNonnullByDefault
public class OutBufferedLogcat extends PureeBufferedOutput {
    public static final String TYPE = "buffered_logcat";

    private static final String TAG = OutBufferedLogcat.class.getSimpleName();

    @Override
    public String type() {
        return TYPE;
    }

    @Nonnull
    @Override
    public OutputConfiguration configure(OutputConfiguration conf) {
        conf.setFlushIntervalMillis(2000);
        conf.setLogsPerRequest(3);
        return conf;
    }

    @Override
    public void emit(List<String> jsonLogs, AsyncResult asyncResult) {
        JSONArray jsonLogsArray = new JSONArray();

        for (String jsonLog: jsonLogs) {
            jsonLogsArray.put(jsonLog);
        }

        Log.d(TAG, jsonLogs.toString());

        asyncResult.success();
    }
}
