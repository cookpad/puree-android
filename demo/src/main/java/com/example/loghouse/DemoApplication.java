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
        LogHouseConfiguration conf = new LogHouseConfiguration.Builder(context)
                .beforeInsertAction(new AddRequiredParamsAction())
                .registerOutput(new OutBufferedLogcat())
                .registerOutput(new OutLogcat())
                .build();
        return conf;
    }
}
