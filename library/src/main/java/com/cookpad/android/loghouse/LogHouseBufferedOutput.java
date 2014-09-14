package com.cookpad.android.loghouse;

import com.cookpad.android.loghouse.async.AsyncFlushTask;
import com.cookpad.android.loghouse.async.AsyncInsertTask;
import com.cookpad.android.loghouse.async.AsyncResult;
import com.cookpad.android.loghouse.storage.LogHouseStorage;
import com.cookpad.android.loghouse.storage.Records;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public abstract class LogHouseBufferedOutput extends LogHouseOutput {
    private CuckooClock cuckooClock;

    @Override
    public void initialize(LogHouseConfiguration logHouseConfiguration, LogHouseStorage storage) {
        super.initialize(logHouseConfiguration, storage);
        CuckooClock.OnAlarmListener onAlarmListener = new CuckooClock.OnAlarmListener() {
            @Override
            public void onAlarm() {
                flush();
            }
        };
        cuckooClock = new CuckooClock(onAlarmListener, conf.getFlushInterval());
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
        Records records = getRecordsFromStorage();
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
            records = getRecordsFromStorage();
        }
    }

    private Records getRecordsFromStorage() {
        return storage.select(type(), conf.getLogsPerRequest());
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

