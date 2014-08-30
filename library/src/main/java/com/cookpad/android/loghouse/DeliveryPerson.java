package com.cookpad.android.loghouse;

import java.util.List;

public interface DeliveryPerson {
    public void onShip(List<String> serializedLogs);
}
