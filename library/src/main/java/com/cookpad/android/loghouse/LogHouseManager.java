package com.cookpad.android.loghouse;

import android.content.Context;

import com.cookpad.android.loghouse.handlers.AroundShipFilter;
import com.cookpad.android.loghouse.handlers.DeliveryPerson;
import com.cookpad.android.loghouse.storage.LogHouseDbHelper;
import com.cookpad.android.loghouse.storage.Records;
import com.cookpad.android.loghouse.tasks.IntertAsyncTask;
import com.cookpad.android.loghouse.tasks.ShipAsyncTask;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

public class LogHouseManager {
    private static final String TAG = LogHouseManager.class.getSimpleName();

    private static Context applicationContext;
    private static DeliveryPerson deliveryPerson;
    private static Gson gson;
    private static int logsPerRequest;
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
        logsPerRequest = conf.getLogsPerRequest();
        CuckooClock.setup(onAlarmListener, conf.getShipIntervalTime(), conf.getShipIntervalTimeUnit());
        aroundShipFilter = conf.getAroundShipFilter();
        logHouseStorage = new LogHouseDbHelper(conf.getApplicationContext());
    }

    public static void ask(Log log) {
        ask(log.toJSON(gson));
    }

    public static void ask(JSONObject serializedLog) {
        new IntertAsyncTask(serializedLog).execute();
    }

    public static void insertSync(JSONObject serializedLog) {
        logHouseStorage.insert(serializedLog);
        CuckooClock.setAlarm(applicationContext);
    }

    public static void ship() {
        ship(logsPerRequest);
    }

    public static void ship(int logsPerRequest) {
        new ShipAsyncTask(logsPerRequest).execute();
    }

    public static void shipSync(int logsPerRequest) {
        Records records = logHouseStorage.select(logsPerRequest);
        if (records.isEmpty()) {
            return;
        }

        while (!records.isEmpty()) {
            List<JSONObject> serializedLogs = records.getSerializedLogs();
            serializedLogs = aroundShipFilter.beforeShip(serializedLogs);
            // TODO: validate logs
            deliveryPerson.onShip(serializedLogs);
            aroundShipFilter.afterShip(serializedLogs);

            logHouseStorage.delete(records);
            records = logHouseStorage.select(logsPerRequest);
        }
    }
}
