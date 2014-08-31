package com.cookpad.android.loghouse;

import android.content.Context;

import com.cookpad.android.loghouse.handlers.BeforeInsertFilter;
import com.cookpad.android.loghouse.handlers.BeforeShipFilter;
import com.cookpad.android.loghouse.handlers.DeliveryPerson;
import com.cookpad.android.loghouse.storage.LogHouseDbHelper;
import com.cookpad.android.loghouse.storage.Records;
import com.cookpad.android.loghouse.tasks.IntertAsyncTask;
import com.cookpad.android.loghouse.tasks.ShipAsyncTask;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LogHouseManager {
    private static final String TAG = LogHouseManager.class.getSimpleName();

    private static Context applicationContext;
    private static DeliveryPerson deliveryPerson;
    private static Gson gson;
    private static int logsPerRequest;
    private static BeforeInsertFilter beforeInsertFilter;
    private static BeforeShipFilter beforeShipFilter;
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
        beforeInsertFilter = conf.getBeforeInsertFilter();
        beforeShipFilter = conf.getBeforeShipFilter();
        logHouseStorage = new LogHouseDbHelper(conf.getApplicationContext());
    }

    public static void ask(Log log) {
        ask(log.toJSON(gson));
    }

    public static void ask(JSONObject serializedLog) {
        new IntertAsyncTask(serializedLog).execute();
        CuckooClock.setAlarm(applicationContext);
    }

    public static void insertSync(JSONObject serializedLog) {
        if (beforeInsertFilter != null) {
            try {
                serializedLog = beforeInsertFilter.beforeInsert(serializedLog);
            } catch (JSONException e) {
                // TODO: notify error
                return;
            }
        }
        logHouseStorage.insert(serializedLog);
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

            if (beforeShipFilter != null) {
                serializedLogs = beforeShipFilter.beforeShip(serializedLogs);
            }

            // TODO: validate logs

            boolean isShipSucceeded = deliveryPerson.onShip(serializedLogs);
            if (!isShipSucceeded) {
                // TODO: retry later
                break;
            }

            logHouseStorage.delete(records);
            records = logHouseStorage.select(logsPerRequest);
        }
    }
}
