package com.example.loghouse;

import android.app.Application;
import android.util.Log;

import com.cookpad.android.loghouse.handlers.AroundShipFilter;
import com.cookpad.android.loghouse.handlers.DeliveryPerson;
import com.cookpad.android.loghouse.LogHouseConfiguration;
import com.cookpad.android.loghouse.LogHouseManager;

import java.util.Calendar;
import java.util.List;

public class DemoApplication extends Application {
    public static final String TAG = DemoApplication.class.getSimpleName();

    private DeliveryPerson deliveryPerson = new DeliveryPerson() {
        @Override
        public void onShip(List<String> serializedLogs) {
            for (String serializedLog : serializedLogs) {
                Log.d(TAG, serializedLog);
            }
        }
    };

    private AroundShipFilter aroundShipFilter = new AroundShipFilter() {
        @Override
        public List<String> beforeShip(List<String> serializedLogs) {
            Log.d(TAG, "beforeShip is called");
            return serializedLogs;
        }

        @Override
        public void afterShip(List<String> serializedLogs) {
            Log.d(TAG, "afterShip is called");
        }
    };

    @Override
    public void onCreate() {
        LogHouseConfiguration conf = new LogHouseConfiguration.Builder(this, deliveryPerson)
                .shipInterval(3, Calendar.SECOND)
                .aroundShipFilter(aroundShipFilter)
                .build();
        LogHouseManager.initialize(conf);
    }
}
