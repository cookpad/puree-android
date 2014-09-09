package com.cookpad.android.loghouse.async;

import android.os.AsyncTask;

import com.cookpad.android.loghouse.LogHouseBufferedOutput;

public class AsyncFlushTask extends AsyncTask<Void, Void, Void> {
    private LogHouseBufferedOutput output;

    public AsyncFlushTask(LogHouseBufferedOutput output) {
        this.output = output;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        output.flushSync();
        return null;
    }
}
