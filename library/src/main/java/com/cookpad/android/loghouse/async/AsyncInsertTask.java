package com.cookpad.android.loghouse.async;

import android.os.AsyncTask;

import com.cookpad.android.loghouse.LogHouse;
import com.cookpad.android.loghouse.LogHouseBufferedOutput;

import org.json.JSONObject;

public class AsyncInsertTask extends AsyncTask<Void, Void, Void> {
    private LogHouseBufferedOutput output;
    private String type;
    private JSONObject serializedLog;

    public AsyncInsertTask(LogHouseBufferedOutput output, String type, JSONObject serializedLog) {
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
