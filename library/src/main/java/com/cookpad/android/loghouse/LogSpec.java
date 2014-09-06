package com.cookpad.android.loghouse;

import com.cookpad.android.loghouse.handlers.AfterShipAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class LogSpec {
    private LogHouseConfiguration conf;
    private List<Log> logs;

    public LogSpec(LogHouseConfiguration conf) {
        this.conf = conf;
    }

    public LogSpec logs(Log... logs) {
        return logs(Arrays.asList(logs));
    }

    public LogSpec logs(List<Log> logs) {
        this.logs = logs;
        return this;
    }

    public void shouldBe(Matcher matcher) {
        final CountDownLatch latch = new CountDownLatch(logs.size());
        final List<JSONObject> results = new ArrayList<JSONObject>();

        AfterShipAction afterShipAction = new AfterShipAction() {
            @Override
            public void call(List<JSONObject> serializedLogs) {
                results.addAll(serializedLogs);
                latch.countDown();
            }
        };

        conf.setAfterShipAction(afterShipAction);
        LogHouse.initialize(conf);

        for (Log log : logs) {
            LogHouse.ask(log);
        }

        try {
            matcher.expect(results);
        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static interface Matcher {
        public void expect(List<JSONObject> serializedLogs) throws JSONException;
    }
}
