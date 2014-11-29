package com.example.puree.logs.plugins;

import android.os.Handler;
import android.os.Looper;

import com.cookpad.puree.outputs.OutputConfiguration;
import com.cookpad.puree.outputs.PureeBufferedOutput;
import com.cookpad.puree.async.AsyncResult;

import org.json.JSONArray;

import java.lang.ref.WeakReference;

public class OutBufferedDisplay extends PureeBufferedOutput {
    public static final String TYPE = "buffered_display";

    private static WeakReference<Callback> callbackRef = new WeakReference<>(null);

    public static void register(Callback callback) {
        callbackRef = new WeakReference<>(callback);
    }

    public static void unregister() {
        callbackRef.clear();
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public OutputConfiguration configure(OutputConfiguration conf) {
        conf.setFlushIntervalMillis(3000);
        return conf;
    }

    @Override
    public void emit(final JSONArray jsonLogs, final AsyncResult result) {
        final Callback callback = callbackRef.get();
        if (callback == null) {
            result.success();
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                callback.onEmit(jsonLogs);
                result.success();
            }
        });
    }

    public static interface Callback {
        public void onEmit(JSONArray jsonLogs);
    }
}
