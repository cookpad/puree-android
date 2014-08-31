package com.cookpad.android.loghouse.handlers;

import org.json.JSONObject;

import java.util.List;

public interface DeliveryPerson {
    public boolean onShip(List<JSONObject> serializedLogs);
}
