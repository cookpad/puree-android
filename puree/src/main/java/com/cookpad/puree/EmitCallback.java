package com.cookpad.puree;

import org.json.JSONArray;

public abstract class EmitCallback {
    public static final EmitCallback DEFAULT = new EmitCallback() {
        @Override
        public void call(String type, JSONArray serializedLogs) {
            // do nothing
        }
    };
    public abstract void call(String type, JSONArray serializedLogs);
}
