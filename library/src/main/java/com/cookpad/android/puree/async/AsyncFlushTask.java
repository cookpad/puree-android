package com.cookpad.android.puree.async;

import android.os.AsyncTask;

import com.cookpad.android.puree.LogHouseBufferedOutput;

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
