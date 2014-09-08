package com.cookpad.android.loghouse.async;

import android.os.AsyncTask;

import com.cookpad.android.loghouse.LogHouse;

public class AsyncFlushTask extends AsyncTask<Void, Void, Void> {
    private LogHouse.BufferedOutput output;

    public AsyncFlushTask(LogHouse.BufferedOutput output) {
        this.output = output;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        output.flushSync();
        return null;
    }
}
