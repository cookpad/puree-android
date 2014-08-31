package com.cookpad.android.loghouse.handlers;

import org.json.JSONObject;

import java.util.List;

public interface DeliveryPerson {
    public void onShip(List<JSONObject> serializedLogs);
}
