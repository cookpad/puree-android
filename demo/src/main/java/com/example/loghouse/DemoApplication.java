package com.example.loghouse;

import android.app.Application;
import android.util.Log;

import com.cookpad.android.loghouse.handlers.BeforeShipAction;
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

    private BeforeShipAction beforeShipAction = new BeforeShipAction() {
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
                .beforeInsertFilter(new AddRequiredParamsAction())
                .beforeShipFilter(beforeShipAction)
                .build();
        LogHouseManager.initialize(conf);
    }
}
