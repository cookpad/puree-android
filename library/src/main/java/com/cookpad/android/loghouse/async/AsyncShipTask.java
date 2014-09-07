package com.cookpad.android.loghouse.async;

import android.os.AsyncTask;

import com.cookpad.android.loghouse.LogHouse;

public class AsyncShipTask extends AsyncTask<Void, Void, Void> {
    private LogHouse.BufferedOutput output;
    private int logsPerRequest;

    public AsyncShipTask(LogHouse.BufferedOutput output, int logsPerRequest) {
        this.output = output;
        this.logsPerRequest = logsPerRequest;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        output.shipSync(logsPerRequest);
        return null;
    }
}
