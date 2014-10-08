package com.cookpad.android.puree;

import com.cookpad.android.puree.async.AsyncFlushTask;
import com.cookpad.android.puree.async.AsyncInsertTask;
import com.cookpad.android.puree.async.AsyncResult;
import com.cookpad.android.puree.lazy.LazyTask;
import com.cookpad.android.puree.lazy.LazyTaskRunner;
import com.cookpad.android.puree.storage.PureeStorage;
import com.cookpad.android.puree.storage.Records;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public abstract class PureeBufferedOutput extends PureeOutput {
    private LazyTaskRunner lazyTaskRunner;

    @Override
    public void initialize(PureeStorage storage) {
        super.initialize(storage);
        lazyTaskRunner = new LazyTaskRunner(new LazyTask() {
            @Override
            public void run() {
                flush();
            }
        }, conf.getFlushInterval());
    }

    @Override
    public void start(JSONObject serializedLog) {
        if (PureeConfiguration.isTest) {
            insertSync(type(), serializedLog);
            flushSync();
        } else {
            new AsyncInsertTask(this, type(), serializedLog).execute();
            lazyTaskRunner.tryToStart();
        }
    }

    public void insertSync(String type, JSONObject serializedLog) {
        try {
            serializedLog = applyBeforeFilters(serializedLog);
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
            if (!PureeConfiguration.isTest) {
                boolean isSuccess = flushChunkOfLogs(serializedLogs);
                if (isSuccess) {
                    lazyTaskRunner.reset();
                } else {
                    lazyTaskRunner.retryLater();
                    return;
                }
            }
            applyAfterFilters(type(), serializedLogs);
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

