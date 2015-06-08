package com.cookpad.puree.async;

import com.google.gson.JsonObject;

import com.cookpad.puree.outputs.PureeBufferedOutput;

import android.os.AsyncTask;

public class AsyncInsertTask extends AsyncTask<Void, Void, Void> {
    private PureeBufferedOutput output;
    private String type;

    private JsonObject jsonLog;

    public AsyncInsertTask(PureeBufferedOutput output, String type, JsonObject jsonLog) {
        this.output = output;
        this.type = type;
        this.jsonLog = jsonLog;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        output.insertSync(type, jsonLog);
        return null;
    }
}
