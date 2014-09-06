package com.cookpad.android.loghouse.async;

import android.os.AsyncTask;

import com.cookpad.android.loghouse.LogHouse;

import org.json.JSONObject;

public class IntertAsyncTask extends AsyncTask<Void, Void, Void> {
    private LogHouse.BufferedOutput output;
    private String type;
    private JSONObject serializedLog;

    public IntertAsyncTask(LogHouse.BufferedOutput output, String type, JSONObject serializedLog) {
        this.output = output;
        this.type = type;
        this.serializedLog = serializedLog;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        output.insertSync(type, serializedLog);
        return null;
    }
}
