package com.cookpad.android.loghouse;

import com.cookpad.android.loghouse.async.AsyncFlushTask;
import com.cookpad.android.loghouse.async.AsyncInsertTask;
import com.cookpad.android.loghouse.async.AsyncResult;
import com.cookpad.android.loghouse.handlers.AfterFlushAction;
import com.cookpad.android.loghouse.handlers.BeforeEmitAction;
import com.cookpad.android.loghouse.storage.LogHouseDbHelper;
import com.cookpad.android.loghouse.storage.LogHouseStorage;
import com.cookpad.android.loghouse.storage.Records;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LogHouse {
    private static Gson gson;
    private static BeforeEmitAction beforeEmitAction;
    private static List<Output> outputs = new ArrayList<>();

    public static void initialize(LogHouseConfiguration conf) {
        gson = conf.getGson();
        beforeEmitAction = conf.getBeforeEmitAction();

        LogHouseStorage logHouseStorage = new LogHouseDbHelper(conf.getApplicationContext());
        if (conf.isTest()) {
            logHouseStorage.clean();
        }

        for (Class<? extends Output> outputType : conf.getOutputTypes()) {
            try {
                Output output = outputType.newInstance();
                output.setStorage(logHouseStorage);
                output.configure(conf);
                outputs.add(output);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Unable to create new instance: " + outputType.getSimpleName());
            }
        }
    }

    public static void in(Log log) {
        in(log.type(), log.toJSON(gson));
    }

    private static synchronized void in(String type, JSONObject serializedLog) {
        for (Output output : outputs) {
            if (output.type().equals(type)) {
                output.start(serializedLog);
            }
        }
    }

    public static abstract class Output {
        protected LogHouseStorage storage;
        protected AfterFlushAction afterFlushAction;
        protected boolean isTest = false;

        public abstract String type();

        public void setStorage(LogHouseStorage storage) {
            this.storage = storage;
        }

        public void configure(LogHouseConfiguration conf) {
            this.isTest = conf.isTest();
            this.afterFlushAction = conf.getAfterFlushAction();
        }

        public void start(JSONObject serializedLog) {
            try {
                serializedLog = beforeEmitAction.call(serializedLog);
                emit(serializedLog);

                List<JSONObject> serializedLogs = new ArrayList<>();
                serializedLogs.add(serializedLog);
                afterFlushAction.call(type(), serializedLogs);
            } catch (JSONException e) {
                // do nothing
            }
        }

        public abstract void emit(JSONObject serializedLog);
    }

    public static abstract class BufferedOutput extends Output {
        private CuckooClock cuckooClock;

        protected int callMeAfter() {
            return 5 * 60 * 1000;
        }

        protected int logsPerRequest() {
            return 1000;
        }

        @Override
        public void configure(LogHouseConfiguration conf) {
            super.configure(conf);
            CuckooClock.OnAlarmListener onAlarmListener = new CuckooClock.OnAlarmListener() {
                @Override
                public void onAlarm() {
                    flush();
                }
            };
            cuckooClock = new CuckooClock(onAlarmListener, callMeAfter());
        }

        @Override
        public void start(JSONObject serializedLog) {
            if (isTest) {
                insertSync(type(), serializedLog);
                flushSync();
            } else {
                new AsyncInsertTask(this, type(), serializedLog).execute();
                cuckooClock.setAlarm();
            }
        }

        public void insertSync(String type, JSONObject serializedLog) {
            try {
                serializedLog = beforeEmitAction.call(serializedLog);
                storage.insert(type, serializedLog);
            } catch (JSONException e) {
                // do nothing
            }
        }

        public void flush() {
            new AsyncFlushTask(this).execute();
        }

        public void flushSync() {
            Records records = storage.select(type(), logsPerRequest());
            if (records.isEmpty()) {
                return;
            }

            while (!records.isEmpty()) {
                final List<JSONObject> serializedLogs = records.getSerializedLogs();
                if (!flushChunkOfLogs(serializedLogs)) {
                    cuckooClock.retryLater();
                    return;
                }
                afterFlushAction.call(type(), serializedLogs);
                storage.delete(records);
                records = storage.select(type(), logsPerRequest());
            }
        }

        public boolean flushChunkOfLogs(final List<JSONObject> serializedLogs) {
            try {
                AsyncResult asyncResult = new AsyncResult();
                emit(serializedLogs, asyncResult);
                return asyncResult.get();
            } catch (InterruptedException e) {
                return false;
            }
        }

        public abstract void emit(List<JSONObject> serializedLogs, final AsyncResult result);

        public void emit(JSONObject serializedLog) {
            // do nothing
        }
    }
}
