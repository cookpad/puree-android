package com.example.loghouse;

import android.app.Application;
import android.util.Log;

import com.cookpad.android.loghouse.LogHouse;
import com.cookpad.android.loghouse.handlers.BeforeShipAction;
import com.cookpad.android.loghouse.LogHouseConfiguration;

import org.json.JSONObject;

import java.util.List;

public class DemoApplication extends Application {
    public static final String TAG = DemoApplication.class.getSimpleName();

    private BeforeShipAction beforeShipAction = new BeforeShipAction() {
        @Override
        public List<JSONObject> call(List<JSONObject> serializedLogs) {
            Log.d(TAG, "BeforeShipAction.call is called");
            return serializedLogs;
        }
    };

    @Override
    public void onCreate() {
        LogHouseConfiguration conf = new LogHouseConfiguration.Builder(this)
                .logsPerRequest(3)
                .beforeInsertAction(new AddRequiredParamsAction())
                .beforeShipAction(beforeShipAction)
                .build();
        LogHouse.initialize(conf);
    }
}
