package com.cookpad.android.puree.async;

import android.os.AsyncTask;

import com.cookpad.android.puree.outputs.PureeBufferedOutput;

public class AsyncFlushTask extends AsyncTask<Void, Void, Void> {
    private PureeBufferedOutput output;

    public AsyncFlushTask(PureeBufferedOutput output) {
        this.output = output;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        output.flushSync();
        return null;
    }
}
