package com.cookpad.android.puree;

import org.json.JSONObject;

import java.util.List;

public abstract class EmitCallback {
    public static final EmitCallback DEFAULT = new EmitCallback() {
        @Override
        public void call(String type, List<JSONObject> serializedLogs) {
            // do nothing
        }
    };
    public abstract void call(String type, List<JSONObject> serializedLogs);
}
