package com.cookpad.puree.outputs;

import com.cookpad.puree.async.AsyncFlushTask;
import com.cookpad.puree.async.AsyncInsertTask;
import com.cookpad.puree.async.AsyncResult;
import com.cookpad.puree.retryable.RetryableTaskRunner;
import com.cookpad.puree.storage.PureeStorage;
import com.cookpad.puree.storage.Records;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class PureeBufferedOutput extends PureeOutput {
    private RetryableTaskRunner retryableTaskRunner;

    @Override
    public void initialize(PureeStorage storage) {
        super.initialize(storage);
        retryableTaskRunner = new RetryableTaskRunner(new Runnable() {
            @Override
            public void run() {
                flush();
            }
        }, conf.getFlushIntervalMillis(), conf.getMaxRetryCount());
    }

    @Override
    public void receive(JSONObject jsonLog) {
        new AsyncInsertTask(this, type(), jsonLog).execute();
        retryableTaskRunner.tryToStart();
    }

    public void insertSync(String type, JSONObject jsonLog) {
        try {
            JSONObject filteredLog = applyFilters(jsonLog);
            storage.insert(type, filteredLog);
        } catch (JSONException e) {
            // do nothing
        }
    }

    @Override
    public void flush() {
        new AsyncFlushTask(this).execute();
    }

    public void flushSync() {
        Records records = getRecordsFromStorage();
        if (records.isEmpty()) {
            return;
        }

        while (!records.isEmpty()) {
            final JSONArray jsonLogs = records.getJsonLogs();
            boolean isSuccess = flushChunkOfLogs(jsonLogs);
            if (isSuccess) {
                retryableTaskRunner.reset();
            } else {
                retryableTaskRunner.retryLater();
                return;
            }
            storage.delete(records);
            records = getRecordsFromStorage();
        }
    }

    private Records getRecordsFromStorage() {
        return storage.select(type(), conf.getLogsPerRequest());
    }

    public boolean flushChunkOfLogs(final JSONArray jsonLogs) {
        try {
            AsyncResult asyncResult = new AsyncResult();
            emit(jsonLogs, asyncResult);
            return asyncResult.get();
        } catch (InterruptedException e) {
            return false;
        }
    }

    public abstract void emit(JSONArray jsonArray, final AsyncResult result);

    public void emit(JSONObject jsonLog) {
        // do nothing
    }
}

