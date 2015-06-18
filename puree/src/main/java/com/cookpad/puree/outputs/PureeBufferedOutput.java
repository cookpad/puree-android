package com.cookpad.puree.outputs;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.cookpad.puree.async.AsyncResult;
import com.cookpad.puree.internal.RetryableTaskRunner;
import com.cookpad.puree.storage.PureeStorage;
import com.cookpad.puree.storage.Records;

import android.os.AsyncTask;
import android.os.Handler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class PureeBufferedOutput extends PureeOutput {

    private RetryableTaskRunner flushTask;

    private final Handler handler;

    public PureeBufferedOutput(Handler handler) {
        this.handler = handler;
    }

    public PureeBufferedOutput() {
        this(new Handler());
    }

    @Override
    public void initialize(PureeStorage storage) {
        super.initialize(storage);
        flushTask = new RetryableTaskRunner(new Runnable() {
            @Override
            public void run() {
                flush();
            }
        }, conf.getFlushIntervalMillis(), conf.getMaxRetryCount(), handler);
    }

    @Override
    public void receive(final JsonObject jsonLog) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                JsonObject filteredLog = applyFilters(jsonLog);
                if (filteredLog != null) {
                    storage.insert(type(), filteredLog);
                }
                return null;
            }
        }.execute();

        flushTask.tryToStart();
    }

    @Override
    public void flush() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                flushSync();
                return null;
            }
        }.execute();

    }

    public void flushSync() {
        Records records = getRecordsFromStorage();

        if (records.isEmpty()) {
            return;
        }

        final JsonArray jsonLogs = records.getJsonLogs();
        boolean isSuccess = flushChunkOfLogs(jsonLogs);
        if (isSuccess) {
            flushTask.reset();
            storage.delete(records);
        } else {
            flushTask.retryLater();
        }
    }

    private Records getRecordsFromStorage() {
        return storage.select(type(), conf.getLogsPerRequest());
    }

    public boolean flushChunkOfLogs(final JsonArray jsonLogs) {
        try {
            AsyncResult asyncResult = new AsyncResult();
            emit(jsonLogs, asyncResult);
            return asyncResult.get();
        } catch (InterruptedException e) {
            return false;
        }
    }

    public abstract void emit(JsonArray jsonArray, final AsyncResult result);

    public void emit(JsonObject jsonLog) {
        // do nothing
    }
}

