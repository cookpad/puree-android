package com.cookpad.android.loghouse.handlers;

import org.json.JSONObject;

import java.util.List;

public interface BeforeShipAction {
    public static final BeforeShipAction DEFAULT = new BeforeShipAction() {
        @Override
        public List<JSONObject> beforeShip(List<JSONObject> serializedLogs) {
            return serializedLogs;
        }
    };

    public List<JSONObject> beforeShip(List<JSONObject> serializedLogs);
}
