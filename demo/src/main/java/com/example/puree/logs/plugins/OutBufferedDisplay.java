package com.example.puree.logs.plugins;

import android.os.Handler;
import android.os.Looper;

import com.cookpad.android.puree.LogHouseBufferedOutput;
import com.cookpad.android.puree.async.AsyncResult;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

public class OutBufferedDisplay extends LogHouseBufferedOutput {
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
    public Configuration configure(Configuration conf) {
        conf.setFlushInterval(3000);
        return conf;
    }

    @Override
    public void emit(final List<JSONObject> serializedLogs, final AsyncResult result) {
        final Callback callback = callbackRef.get();
        if (callback == null) {
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                callback.onEmit(serializedLogs);
                result.success();
            }
        });
    }

    public static interface Callback {
        public void onEmit(List<JSONObject> serializedLogs);
    }
}
