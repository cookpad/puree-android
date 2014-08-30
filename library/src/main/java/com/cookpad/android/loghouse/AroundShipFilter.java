package com.cookpad.android.loghouse;

import java.util.List;

public class AroundShipFilter {
    public List<String> beforeShip(List<String> serializedLogs) {
        return serializedLogs;
    }

    public void afterShip(List<String> serializedLogs) {}
}
