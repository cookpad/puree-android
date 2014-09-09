package com.example.loghouse;

import android.app.Application;
import android.content.Context;

import com.cookpad.android.loghouse.LogHouse;
import com.cookpad.android.loghouse.LogHouseConfiguration;
import com.cookpad.android.loghouse.plugins.OutBufferedLogcat;
import com.cookpad.android.loghouse.plugins.OutLogcat;

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        LogHouse.initialize(buildConfiguration(this));
    }

    public static LogHouseConfiguration buildConfiguration(Context context) {
        return new LogHouseConfiguration.Builder(context)
                .beforeEmitAction(new AddRequiredParamsAction())
                .registerOutput(OutBufferedLogcat.class)
                .registerOutput(OutLogcat.class)
                .build();
    }
}
