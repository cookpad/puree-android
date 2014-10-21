package com.cookpad.android.puree.async;

import android.os.AsyncTask;

import com.cookpad.android.puree.outputs.PureeBufferedOutput;

import org.json.JSONObject;

public class AsyncInsertTask extends AsyncTask<Void, Void, Void> {
    private PureeBufferedOutput output;
    private String type;
    private JSONObject serializedLog;

    public AsyncInsertTask(PureeBufferedOutput output, String type, JSONObject serializedLog) {
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
