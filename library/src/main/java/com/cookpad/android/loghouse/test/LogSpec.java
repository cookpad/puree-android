package com.cookpad.android.loghouse.test;

import android.content.Context;

import com.cookpad.android.loghouse.Log;
import com.cookpad.android.loghouse.LogHouseConfiguration;
import com.cookpad.android.loghouse.LogHouseManager;
import com.cookpad.android.loghouse.async.ShipExecutor;
import com.cookpad.android.loghouse.handlers.BeforeInsertAction;
import com.cookpad.android.loghouse.handlers.BeforeShipAction;
import com.cookpad.android.loghouse.handlers.DeliveryPerson;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class LogSpec {
    private Context context;
    private Gson gson = new Gson();
    private BeforeInsertAction beforeInsertAction;
    private List<Log> logs;

    private static final DeliveryPerson DELIVERY_PERSON = new DeliveryPerson() {
        @Override
        public boolean onShip(List<JSONObject> serializedLogs) {
            return true;
        }
    };

    public LogSpec(Context context) {
        this.context = context;
    }

    public LogSpec gson(Gson gson) {
        this.gson = gson;
        return this;
    }

    public LogSpec action(BeforeInsertAction beforeInsertAction) {
        this.beforeInsertAction = beforeInsertAction;
        return this;
    }

    public LogSpec logs(Log... logs) {
        return logs(Arrays.asList(logs));
    }

    public LogSpec logs(List<Log> logs) {
        this.logs = logs;
        return this;
    }

    public void shouldBe(final Matcher matcher) {
        BeforeShipAction beforeShipAction = new BeforeShipAction() {
            @Override
            public List<JSONObject> beforeShip(List<JSONObject> serializedLogs) {
                try {
                    matcher.expect(serializedLogs);
                    return serializedLogs;
                } catch (JSONException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        };

        LogHouseConfiguration conf = new LogHouseConfiguration.Builder(context, DELIVERY_PERSON)
                .beforeInsertAction(beforeInsertAction)
                .beforeShipAction(beforeShipAction)
                .build();

        LogHouseManager.initialize(conf);
        for (Log log : logs) {
            LogHouseManager.insertSync(log.toJSON(gson));
        }
        LogHouseManager.shipSync(ShipExecutor.DEFAULT_LOGS_PER_REQUEST);
    }

    public static interface Matcher {
        public void expect(List<JSONObject> serializedLogs) throws JSONException;
    }
}
