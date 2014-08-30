package com.cookpad.android.loghouse;

import com.google.gson.Gson;

import java.util.List;

public class LogHouseManager {
    private static final String TAG = LogHouseManager.class.getSimpleName();
    private static Gson gson;
    private static AroundShipFilter aroundShipFilter;
    private static LogHouseDbHelper logHouseStorage;

    public static void initialize(LogHouseConfiguration conf) {
        gson = conf.getGson();
        aroundShipFilter = conf.getAroundShipFilter();
        logHouseStorage = new LogHouseDbHelper(conf.getApplicationContext());
    }

    public static void ask(Log log) {
        String serializedLog = gson.toJson(log);
        logHouseStorage.insert(serializedLog);
    }

    public static void ship() {
        List<String> serializedLogs = logHouseStorage.select();
        serializedLogs = aroundShipFilter.beforeShip(serializedLogs);
        // validate
        // ship
        aroundShipFilter.afterShip(serializedLogs);
        logHouseStorage.delete();
    }
}
