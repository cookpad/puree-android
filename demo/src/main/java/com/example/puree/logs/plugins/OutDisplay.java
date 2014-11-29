package com.example.puree.logs.plugins;

import com.cookpad.puree.outputs.OutputConfiguration;
import com.cookpad.puree.outputs.PureeOutput;

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
        return "display";
    }

    @Override
    public OutputConfiguration configure(OutputConfiguration conf) {
        return conf;
    }

    @Override
    public void emit(JSONObject jsonLog) {
        Callback callback = callbackRef.get();
        if (callback == null) {
            return;
        }
        callback.onEmit(jsonLog);
    }

    public static interface Callback {
        public void onEmit(JSONObject jsonLog);
    }
}
