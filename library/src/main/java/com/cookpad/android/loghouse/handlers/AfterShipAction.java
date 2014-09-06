package com.cookpad.android.loghouse.handlers;

import org.json.JSONObject;

import java.util.List;

public interface AfterShipAction {
    public static final AfterShipAction DEFAULT = new AfterShipAction() {
        @Override
        public void call(String type, List<JSONObject> serializedLogs) {
        }
    };

    public void call(String type, List<JSONObject> serializedLogs);
}
