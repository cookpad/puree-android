package com.example.puree.logs.plugins;

import com.cookpad.android.puree.PureeOutput;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class OutDisplay extends PureeOutput {
    public static final String TYPE = "display";

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
        return conf;
    }

    @Override
    public void emit(JSONObject serializedLog) {
        Callback callback = callbackRef.get();
        if (callback == null) {
            return;
        }
        callback.onEmit(serializedLog);
    }

    public static interface Callback {
        public void onEmit(JSONObject serializedLog);
    }
}
