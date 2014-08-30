package com.cookpad.android.loghouse;

import android.os.AsyncTask;

class IntertAsyncTask extends AsyncTask<Void, Void, Void> {
    private String serializedLog;

    public IntertAsyncTask(String serializedLog) {
        this.serializedLog = serializedLog;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        LogHouseManager.insertSync(serializedLog);
        return null;
    }
}
