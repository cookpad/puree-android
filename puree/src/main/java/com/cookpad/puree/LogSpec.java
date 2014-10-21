package com.cookpad.puree;

import android.util.Pair;

import com.cookpad.puree.outputs.PureeOutput;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LogSpec {
    private static final Object LOCK = new Object();

    private PureeConfiguration conf;
    private List<Pair<JsonConvertible, String[]>> logs = new ArrayList<>();
    private String targetType;

    public LogSpec(PureeConfiguration conf) {
        this.conf = conf;
    }

    public LogSpec log(final JsonConvertible log, final String... sendTo) {
        this.logs.add(new Pair<>(log, sendTo));
        return this;
    }

    public LogSpec targetType(String targetType) {
        this.targetType = targetType;
        return this;
    }

    public void shouldBe(Matcher matcher) {
        synchronized (LOCK) {
            final CountDownLatch latch = new CountDownLatch(logs.size());
            final JSONArray results = new JSONArray();

            initializePuree(conf);

            Map<String, PureeOutput> outputs = conf.getOutputs();
            for (String type : outputs.keySet()) {
                PureeOutput output = outputs.get(type);
                output.setEmitCallback(new EmitCallback() {
                    @Override
                    public void call(String type, JSONArray serializedLogs) {
                        if (targetType.equals(type)) {
                            for (int i = 0; i < serializedLogs.length(); i++) {
                                try {
                                    results.put(serializedLogs.getJSONObject(i));
                                } catch (JSONException ignored) {
                                }
                            }
                        }
                        latch.countDown();
                    }
                });
            }

            putLogs(logs);

            try {
                latch.await(1000, TimeUnit.MILLISECONDS);
                matcher.expect(results);
            } catch (JSONException | InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    private void putLogs(List<Pair<JsonConvertible, String[]>> logs) {
        for (Pair<JsonConvertible, String[]> log : logs) {
            Puree.send(log.first, log.second);
        }
    }

    private void initializePuree(PureeConfiguration conf) {
        PureeConfiguration.isTest = true;
        Puree.initialize(conf);
        Puree.clear();
    }

    public static interface Matcher {
        public void expect(JSONArray serializedLogs) throws JSONException;
    }
}
