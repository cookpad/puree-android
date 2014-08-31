package com.example.loghouse;

import android.app.Application;
import android.util.Log;

import com.cookpad.android.loghouse.handlers.BeforeInsertFilter;
import com.cookpad.android.loghouse.handlers.BeforeShipFilter;
import com.cookpad.android.loghouse.handlers.DeliveryPerson;
import com.cookpad.android.loghouse.LogHouseConfiguration;
import com.cookpad.android.loghouse.LogHouseManager;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

public class DemoApplication extends Application {
    public static final String TAG = DemoApplication.class.getSimpleName();

    private DeliveryPerson deliveryPerson = new DeliveryPerson() {
        @Override
        public boolean onShip(List<JSONObject> serializedLogs) {
            for (JSONObject serializedLog : serializedLogs) {
                Log.d(TAG, serializedLog.toString());
            }
            return true;
        }
    };

    private BeforeInsertFilter beforeInsertFilter = new BeforeInsertFilter() {
        @Override
        public JSONObject beforeInsert(JSONObject serializedLog) {
            Log.d(TAG, "beforeInsert is called");
            return serializedLog;
        }
    };

    private BeforeShipFilter beforeShipFilter = new BeforeShipFilter() {
        @Override
        public List<JSONObject> beforeShip(List<JSONObject> serializedLogs) {
            Log.d(TAG, "beforeShip is called");
            return serializedLogs;
        }
    };

    @Override
    public void onCreate() {
        LogHouseConfiguration conf = new LogHouseConfiguration.Builder(this, deliveryPerson)
                .logsPerRequest(3)
                .shipInterval(3, Calendar.SECOND)
                .beforeInsertFilter(beforeInsertFilter)
                .beforeShipFilter(beforeShipFilter)
                .build();
        LogHouseManager.initialize(conf);
    }
}
