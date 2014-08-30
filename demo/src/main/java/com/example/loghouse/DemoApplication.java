package com.example.loghouse;

import android.app.Application;

import com.cookpad.android.loghouse.LogHouseConfiguration;
import com.cookpad.android.loghouse.LogHouseManager;

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        LogHouseConfiguration conf = new LogHouseConfiguration.Builder(this)
                .build();
        LogHouseManager.initialize(conf);
    }
}
