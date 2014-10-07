package com.cookpad.android.loghouse;

import com.cookpad.android.loghouse.handlers.AfterFlushFilter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LogSpec {
    private static final Object LOCK = new Object();

    private LogHouseConfiguration conf;
    private List<SerializableLog> logs;
    private String target;

    public LogSpec(LogHouseConfiguration conf) {
        this.conf = conf;
    }

    public LogSpec logs(SerializableLog... logs) {
        return logs(Arrays.asList(logs));
    }

    public LogSpec logs(List<SerializableLog> logs) {
        this.logs = logs;
        return this;
    }

    public LogSpec target(String type) {
        this.target = type;
        return this;
    }

    public void shouldBe(Matcher matcher) {
        synchronized (LOCK) {
            final CountDownLatch latch = new CountDownLatch(logs.size());
            final List<JSONObject> results = new ArrayList<>();

            AfterFlushFilter afterFlushFilter = new AfterFlushFilter() {
                @Override
                public void call(String type, List<JSONObject> serializedLogs) {
                    if (target.equals(type)) {
                        results.addAll(serializedLogs);
                    }
                    latch.countDown();
                }
            };

            conf.setAfterFlushFilter(afterFlushFilter);
            initializeLogHouse();
            putLogs(logs);

            try {
                latch.await(100, TimeUnit.MILLISECONDS);
                matcher.expect(results);
            } catch (JSONException | InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    private void putLogs(List<SerializableLog> logs) {
        for (SerializableLog log : logs) {
            LogHouse.in(log);
        }
    }

    private void initializeLogHouse() {
        LogHouse.initialize(conf);
        LogHouse.clear();
    }

    public static interface Matcher {
        public void expect(List<JSONObject> serializedLogs) throws JSONException;
    }
}
