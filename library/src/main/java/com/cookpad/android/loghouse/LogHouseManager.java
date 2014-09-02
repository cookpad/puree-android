package com.cookpad.android.loghouse;

import android.content.Context;

import com.cookpad.android.loghouse.handlers.AfterShipAction;
import com.cookpad.android.loghouse.handlers.BeforeInsertAction;
import com.cookpad.android.loghouse.handlers.BeforeShipAction;
import com.cookpad.android.loghouse.handlers.DeliveryPerson;
import com.cookpad.android.loghouse.storage.LogHouseDbHelper;
import com.cookpad.android.loghouse.storage.Records;
import com.cookpad.android.loghouse.async.InsertExecutor;
import com.cookpad.android.loghouse.async.ShipExecutor;
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
    private static BeforeInsertAction beforeInsertAction;
    private static BeforeShipAction beforeShipAction;
    private static AfterShipAction afterShipAction;
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
        beforeInsertAction = conf.getBeforeInsertAction();
        beforeShipAction = conf.getBeforeShipAction();
        afterShipAction = conf.getAfterShipAction();

        logHouseStorage = new LogHouseDbHelper(conf.getApplicationContext());
    }

    public static void ask(Log log) {
        ask(log.toJSON(gson));
    }

    public static void ask(JSONObject serializedLog) {
        InsertExecutor.execute(applicationContext, serializedLog);
        CuckooClock.setAlarm(applicationContext);
    }

    public static void insertSync(JSONObject serializedLog) {
        try {
            serializedLog = beforeInsertAction.beforeInsert(serializedLog);
        } catch (JSONException e) {
            // TODO: notify error
            return;
        }
        logHouseStorage.insert(serializedLog);
    }

    public static void ship() {
        ship(logsPerRequest);
    }

    public static void ship(int logsPerRequest) {
        ShipExecutor.execute(applicationContext, logsPerRequest);
    }

    public static void shipSync(int logsPerRequest) {
        Records records = logHouseStorage.select(logsPerRequest);
        if (records.isEmpty()) {
            return;
        }

        while (!records.isEmpty()) {
            List<JSONObject> serializedLogs = records.getSerializedLogs();
            serializedLogs = beforeShipAction.beforeShip(serializedLogs);
            // TODO: validate logs
            boolean isShipSucceeded = deliveryPerson.onShip(serializedLogs);
            afterShipAction.afterShip(serializedLogs);
            if (!isShipSucceeded) {
                // TODO: retry later
                break;
            }

            logHouseStorage.delete(records);
            records = logHouseStorage.select(logsPerRequest);
        }
    }
}
