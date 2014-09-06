package com.cookpad.android.loghouse;

import com.cookpad.android.loghouse.async.IntertAsyncTask;
import com.cookpad.android.loghouse.async.ShipAsyncTask;
import com.cookpad.android.loghouse.handlers.AfterShipAction;
import com.cookpad.android.loghouse.handlers.BeforeInsertAction;
import com.cookpad.android.loghouse.storage.LogHouseDbHelper;
import com.cookpad.android.loghouse.storage.Records;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LogHouse {
    private static final String TAG = LogHouse.class.getSimpleName();

    private static Gson gson;
    private static BeforeInsertAction beforeInsertAction;
    private static List<Output> outputs;
    private static LogHouseDbHelper logHouseStorage;

    public static void initialize(LogHouseConfiguration conf) {
        gson = conf.getGson();
        beforeInsertAction = conf.getBeforeInsertAction();
        outputs = conf.getOutputs();

        for (Output output : outputs) {
            output.configure(conf);
        }

        logHouseStorage = new LogHouseDbHelper(conf.getApplicationContext());
    }

    public static void ask(Log log) {
        ask(log.type(), log.toJSON(gson));
    }

    private static void ask(String type, JSONObject serializedLog) {
        for (Output output : outputs) {
            if (output.type().equals(type)) {
                output.start(serializedLog);
            }
        }
    }

    public static abstract class Output {
        protected AfterShipAction afterShipAction;
        protected boolean isTest = false;

        public abstract String type();

        public void configure(LogHouseConfiguration conf) {
            this.isTest = conf.isTest();
            this.afterShipAction = conf.getAfterShipAction();
        }

        public void start(JSONObject serializedLog) {
            try {
                List<JSONObject> serializedLogs = new ArrayList<JSONObject>();
                serializedLog = beforeInsertAction.call(serializedLog);
                serializedLogs.add(serializedLog);
                emit(serializedLogs);
                afterShipAction.call(type(), serializedLogs);
            } catch (JSONException e) {
                // do nothing
            }
        }

        public abstract boolean emit(List<JSONObject> serializedLogs);
    }

    public static abstract class BufferedOutput extends Output {
        private int callMeAfter = 2000;
        private int logsPerRequest = 1000;
        private CuckooClock cuckooClock;

        @Override
        public void configure(LogHouseConfiguration conf) {
            super.configure(conf);
            CuckooClock.OnAlarmListener onAlarmListener = new CuckooClock.OnAlarmListener() {
                @Override
                public void onAlarm() {
                    ship(logsPerRequest);
                }
            };
            cuckooClock = new CuckooClock(onAlarmListener, callMeAfter);
        }

        @Override
        public void start(JSONObject serializedLog) {
            if (isTest) {
                insertSync(type(), serializedLog);
                shipSync(logsPerRequest);
            } else {
                new IntertAsyncTask(this, type(), serializedLog).execute();
                cuckooClock.setAlarm();
            }
        }

        public void insertSync(String type, JSONObject serializedLog) {
            try {
                serializedLog = beforeInsertAction.call(serializedLog);
                logHouseStorage.insert(type, serializedLog);
            } catch (JSONException e) {
                // do nothing
            }
        }

        public void ship(int logsPerRequest) {
            new ShipAsyncTask(this, logsPerRequest).execute();
        }

        public void shipSync(int logsPerRequest) {
            Records records = logHouseStorage.select(type(), logsPerRequest);
            if (records.isEmpty()) {
                return;
            }

            while (!records.isEmpty()) {
                List<JSONObject> serializedLogs = records.getSerializedLogs();
                boolean isShipSucceeded = emit(serializedLogs);
                afterShipAction.call(type(), serializedLogs);
                if (!isShipSucceeded) {
                    // TODO: retry later
                    break;
                }

                logHouseStorage.delete(records);
                records = logHouseStorage.select(type(), logsPerRequest);
            }
        }
    }
}
