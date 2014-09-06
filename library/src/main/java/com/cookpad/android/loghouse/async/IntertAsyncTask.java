package com.cookpad.android.loghouse.async;

import android.os.AsyncTask;

import com.cookpad.android.loghouse.LogHouse;

import org.json.JSONObject;

public class IntertAsyncTask extends AsyncTask<Void, Void, Void> {
    private LogHouse.BufferedOutput output;
    private JSONObject serializedLog;

    public IntertAsyncTask(LogHouse.BufferedOutput output, JSONObject serializedLog) {
        this.output = output;
        this.serializedLog = serializedLog;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        output.insertSync(serializedLog);
        return null;
    }
}
