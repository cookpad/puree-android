package com.cookpad.android.loghouse.handlers;

import org.json.JSONObject;

import java.util.List;

public interface AfterFlushAction {
    public static final AfterFlushAction DEFAULT = new AfterFlushAction() {
        @Override
        public void call(String type, List<JSONObject> serializedLogs) {
        }
    };

    public void call(String type, List<JSONObject> serializedLogs);
}
