package com.cookpad.android.loghouse.handlers;

import org.json.JSONObject;

import java.util.List;

public class AroundShipFilter {
    public List<JSONObject> beforeShip(List<JSONObject> serializedLogs) {
        return serializedLogs;
    }

    public void afterShip(List<JSONObject> serializedLogs) {}
}
