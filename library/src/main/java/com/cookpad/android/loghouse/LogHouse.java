package com.cookpad.android.loghouse;

import com.cookpad.android.loghouse.async.IntertAsyncTask;
import com.cookpad.android.loghouse.async.ShipAsyncTask;
import com.cookpad.android.loghouse.handlers.AfterShipAction;
import com.cookpad.android.loghouse.handlers.BeforeInsertAction;
import com.cookpad.android.loghouse.handlers.BeforeShipAction;
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
    private static BeforeShipAction beforeShipAction;
    private static List<Output> outputs;
    private static LogHouseDbHelper logHouseStorage;
    public static void initialize(LogHouseConfiguration conf) {
        gson = conf.getGson();
        beforeInsertAction = conf.getBeforeInsertAction();
        beforeShipAction = conf.getBeforeShipAction();
        outputs = conf.getOutputs();

        for (Output output : outputs) {
            output.configure(conf);
        }

        logHouseStorage = new LogHouseDbHelper(conf.getApplicationContext());
    }

    public static void ask(Log log) {
        ask(log.toJSON(gson));
    }

    public static void ask(JSONObject serializedLog) {
        LogHouse.Output output = outputs.get(0);
        output.start(serializedLog);
    }

    public static abstract class Output {
        protected AfterShipAction afterShipAction;

        public void configure(LogHouseConfiguration conf) {
            this.afterShipAction = conf.getAfterShipAction();
        }

        public void start(JSONObject serializedLog) {
            List<JSONObject> serializedLogs = new ArrayList<JSONObject>();
            serializedLogs.add(serializedLog);
            emit(serializedLogs);

            afterShipAction.call(serializedLogs);
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
            new IntertAsyncTask(this, serializedLog).execute();
            cuckooClock.setAlarm();
        }

        public void insertSync(JSONObject serializedLog) {
            try {
                serializedLog = beforeInsertAction.call(serializedLog);
            } catch (JSONException e) {
                // TODO: notify error
                return;
            }
            logHouseStorage.insert(serializedLog);
        }

        public void ship(int logsPerRequest) {
            new ShipAsyncTask(this, logsPerRequest).execute();
        }

        public void shipSync(int logsPerRequest) {
            Records records = logHouseStorage.select(logsPerRequest);
            if (records.isEmpty()) {
                return;
            }

            while (!records.isEmpty()) {
                List<JSONObject> serializedLogs = records.getSerializedLogs();
                serializedLogs = beforeShipAction.call(serializedLogs);

                boolean isShipSucceeded = emit(serializedLogs);
                afterShipAction.call(serializedLogs);
                if (!isShipSucceeded) {
                    // TODO: retry later
                    break;
                }


                logHouseStorage.delete(records);
                records = logHouseStorage.select(logsPerRequest);
            }
        }
    }
}
