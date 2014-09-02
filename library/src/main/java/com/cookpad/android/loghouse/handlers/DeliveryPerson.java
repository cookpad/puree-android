package com.cookpad.android.loghouse.handlers;

import com.cookpad.android.loghouse.exceptions.DeliveryPersonUndefinedException;

import org.json.JSONObject;

import java.util.List;

public interface DeliveryPerson {
    public static final DeliveryPerson DEFAULT = new DeliveryPerson() {
        @Override
        public boolean onShip(List<JSONObject> serializedLogs) {
            throw new DeliveryPersonUndefinedException();
        }
    };

    public boolean onShip(List<JSONObject> serializedLogs);
}
