package com.cookpad.android.puree;

import com.cookpad.android.puree.handlers.AfterFlushFilter;

import junit.framework.AssertionFailedError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LogSpec {
    private static final Object LOCK = new Object();

    private PureeConfiguration conf;
    private List<SerializableLog> logs;
    private String target;

    public LogSpec(PureeConfiguration conf) {
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

            final String[] compareInfoMessage = {"[compare] target : type\n"};
            conf.setAfterFlushFilter(new AfterFlushFilter() {
                @Override
                public void call(String type, List<JSONObject> serializedLogs) {
                    compareInfoMessage[0] += "    " + target + " : " + type + "\n";

                    if (target.equals(type)) {
                        results.addAll(serializedLogs);
                    }
                    latch.countDown();
                }
            });

            initializePuree(conf);
            putLogs(logs);

            try {
                latch.await(1000, TimeUnit.MILLISECONDS);
                matcher.expect(results);
            } catch (AssertionFailedError e) {
                throw new AssertionFailedError(e.getMessage() + "\n"
                        + compareInfoMessage[0]
                        + "[result size] " + results.size());
            } catch (JSONException | InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    private void putLogs(List<SerializableLog> logs) {
        for (SerializableLog log : logs) {
            Puree.in(log);
        }
    }

    private void initializePuree(PureeConfiguration conf) {
        Puree.initialize(conf);
        Puree.clear();
    }

    public static interface Matcher {
        public void expect(List<JSONObject> serializedLogs) throws JSONException;
    }
}
