package com.cookpad.android.loghouse.async;

import android.os.AsyncTask;

import com.cookpad.android.loghouse.LogHouseManager;

import org.json.JSONObject;

public class IntertAsyncTask extends AsyncTask<Void, Void, Void> {
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
