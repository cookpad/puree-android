package com.cookpad.android.loghouse;

import android.os.AsyncTask;

import org.json.JSONObject;

class IntertAsyncTask extends AsyncTask<Void, Void, Void> {
    private JSONObject serializedLog;

    public IntertAsyncTask(JSONObject serializedLog) {
        this.serializedLog = serializedLog;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        LogHouseManager.insertSync(serializedLog);
        return null;
    }
}
