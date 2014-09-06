package com.example.loghouse;

import android.app.Application;

import com.cookpad.android.loghouse.LogHouse;
import com.cookpad.android.loghouse.LogHouseConfiguration;
import com.cookpad.android.loghouse.plugins.OutBufferedLogcat;
import com.cookpad.android.loghouse.plugins.OutLogcat;

public class DemoApplication extends Application {
    public static final String TAG = DemoApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        LogHouseConfiguration conf = new LogHouseConfiguration.Builder(this)
                .beforeInsertAction(new AddRequiredParamsAction())
                .registerOutput(new OutBufferedLogcat())
                .registerOutput(new OutLogcat())
                .build();
        LogHouse.initialize(conf);
    }
}
