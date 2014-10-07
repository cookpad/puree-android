package com.cookpad.android.loghouse.handlers;

import org.json.JSONObject;

import java.util.List;

public interface AfterFlushFilter {
    public static final AfterFlushFilter DEFAULT = new AfterFlushFilter() {
        @Override
        public void call(String type, List<JSONObject> serializedLogs) {
        }
    };

    public void call(String type, List<JSONObject> serializedLogs);
}
