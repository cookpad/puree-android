package com.example.loghouse;

import android.app.Application;
import android.util.Log;

import com.cookpad.android.loghouse.AroundShipFilter;
import com.cookpad.android.loghouse.LogHouseConfiguration;
import com.cookpad.android.loghouse.LogHouseManager;

import java.util.Calendar;
import java.util.List;

public class DemoApplication extends Application {
    public static final String TAG = DemoApplication.class.getSimpleName();

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
        LogHouseConfiguration conf = new LogHouseConfiguration.Builder(this)
                .shipInterval(3, Calendar.SECOND)
                .aroundShipFilter(aroundShipFilter)
                .build();
        LogHouseManager.initialize(conf);
    }
}
