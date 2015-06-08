package com.cookpad.puree.plugins;

import com.google.gson.JsonArray;

import com.cookpad.puree.async.AsyncResult;
import com.cookpad.puree.outputs.OutputConfiguration;
import com.cookpad.puree.outputs.PureeBufferedOutput;

import android.util.Log;

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
    public void emit(JsonArray jsonLogs, AsyncResult asyncResult) {
        Log.d(TAG, jsonLogs.toString());
        asyncResult.success();
    }
}
