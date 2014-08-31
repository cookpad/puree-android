package com.cookpad.android.loghouse.tasks;

import android.os.AsyncTask;

import com.cookpad.android.loghouse.LogHouseManager;

public class ShipAsyncTask extends AsyncTask<Void, Void, Void> {
    private int logsPerRequest;

    public ShipAsyncTask(int logsPerRequest) {
        this.logsPerRequest = logsPerRequest;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        LogHouseManager.shipSync(logsPerRequest);
        return null;
    }
}
