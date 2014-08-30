package com.cookpad.android.loghouse;

import android.content.Context;

import com.google.gson.Gson;

import java.util.List;

public class LogHouseManager {
    private static final String TAG = LogHouseManager.class.getSimpleName();

    private static Context applicationContext;
    private static DeliveryPerson deliveryPerson;
    private static Gson gson;
    private static AroundShipFilter aroundShipFilter;
    private static LogHouseDbHelper logHouseStorage;
    private static CuckooClock.OnAlarmListener onAlarmListener = new CuckooClock.OnAlarmListener() {
        @Override
        public void onAlarm() {
            ship();
        }
    };

    public static void initialize(LogHouseConfiguration conf) {
        applicationContext = conf.getApplicationContext();
        deliveryPerson = conf.getDeliveryPerson();
        gson = conf.getGson();
        CuckooClock.setup(onAlarmListener, conf.getShipIntervalTime(), conf.getShipIntervalTimeUnit());
        aroundShipFilter = conf.getAroundShipFilter();
        logHouseStorage = new LogHouseDbHelper(conf.getApplicationContext());
    }

    public static void ask(Log log) {
        String serializedLog = gson.toJson(log);
        logHouseStorage.insert(serializedLog);
        CuckooClock.setAlarm(applicationContext);
    }

    public static void ship() {
        List<String> serializedLogs = logHouseStorage.select();
        serializedLogs = aroundShipFilter.beforeShip(serializedLogs);
        // validate
        deliveryPerson.onShip(serializedLogs);
        aroundShipFilter.afterShip(serializedLogs);
        logHouseStorage.delete();
    }
}
