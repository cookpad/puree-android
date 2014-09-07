package com.cookpad.android.loghouse.async;

import android.os.AsyncTask;

import com.cookpad.android.loghouse.LogHouse;

public class AsyncShipTask extends AsyncTask<Void, Void, Void> {
    private LogHouse.BufferedOutput output;

    public AsyncShipTask(LogHouse.BufferedOutput output) {
        this.output = output;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        output.shipSync();
        return null;
    }
}
