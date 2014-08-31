package com.cookpad.android.loghouse.handlers;

import org.json.JSONObject;

import java.util.List;

public interface BeforeShipFilter {
    public List<JSONObject> beforeShip(List<JSONObject> serializedLogs);
}
