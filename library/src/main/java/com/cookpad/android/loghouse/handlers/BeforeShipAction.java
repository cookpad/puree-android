package com.cookpad.android.loghouse.handlers;

import org.json.JSONObject;

import java.util.List;

public interface BeforeShipAction {
    public List<JSONObject> beforeShip(List<JSONObject> serializedLogs);
}
