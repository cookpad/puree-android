package com.cookpad.android.loghouse.test;

import android.content.Context;

import com.cookpad.android.loghouse.Log;
import com.cookpad.android.loghouse.LogHouse;
import com.cookpad.android.loghouse.LogHouseConfiguration;
import com.cookpad.android.loghouse.handlers.AfterShipAction;
import com.cookpad.android.loghouse.handlers.BeforeInsertAction;
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

    public LogSpec(Context context) {
        this.context = context;
    }

    public LogSpec gson(Gson gson) {
        this.gson = gson;
        return this;
    }

    public LogSpec beforeInsertAction(BeforeInsertAction beforeInsertAction) {
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
        AfterShipAction afterShipAction = new AfterShipAction() {
            @Override
            public void call(List<JSONObject> serializedLogs) {
                try {
                    matcher.expect(serializedLogs);
                } catch (JSONException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        };

        LogHouseConfiguration conf = new LogHouseConfiguration.Builder(context)
                .beforeInsertAction(beforeInsertAction)
                .afterShipAction(afterShipAction)
                .build();

        LogHouse.initialize(conf);
        // TODO: impl later
//        for (Log log : logs) {
//            LogHouse.insertSync(log.toJSON(gson));
//        }
//        LogHouse.shipSync(ShipExecutor.DEFAULT_LOGS_PER_REQUEST);
    }

    public static interface Matcher {
        public void expect(List<JSONObject> serializedLogs) throws JSONException;
    }
}
