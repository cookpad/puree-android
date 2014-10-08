package com.cookpad.android.puree;

import com.cookpad.android.puree.handlers.AfterFlushFilter;
import com.cookpad.android.puree.handlers.BeforeEmitFilter;
import com.cookpad.android.puree.storage.LogHouseStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class LogHouseOutput {
    protected Configuration conf;
    protected LogHouseStorage storage;
    protected AfterFlushFilter afterFlushFilter;
    protected BeforeEmitFilter beforeEmitFilter;

    public void initialize(LogHouseConfiguration logHouseConfiguration, LogHouseStorage storage) {
        this.afterFlushFilter = logHouseConfiguration.getAfterFlushFilter();
        this.beforeEmitFilter = logHouseConfiguration.getBeforeEmitFilter();
        this.storage = storage;
        this.conf = configure(new Configuration());
    }

    public void start(JSONObject serializedLog) {
        try {
            serializedLog = beforeEmitFilter.call(serializedLog);
            emit(serializedLog);

            List<JSONObject> serializedLogs = new ArrayList<>();
            serializedLogs.add(serializedLog);
            afterFlushFilter.call(type(), serializedLogs);
        } catch (JSONException e) {
            // do nothing
        }
    }

    public abstract String type();

    public abstract Configuration configure(Configuration conf);

    public abstract void emit(JSONObject serializedLog);

    public static class Configuration {
        private int flushInterval = 2 * 60 * 1000;
        private int logsPerRequest = 100;

        public int getFlushInterval() {
            return flushInterval;
        }

        public void setFlushInterval(int flushInterval) {
            this.flushInterval = flushInterval;
        }

        public int getLogsPerRequest() {
            return logsPerRequest;
        }

        public void setLogsPerRequest(int logsPerRequest) {
            this.logsPerRequest = logsPerRequest;
        }
    }
}

