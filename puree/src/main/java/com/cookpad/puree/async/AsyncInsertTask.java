package com.cookpad.puree.async;

import android.os.AsyncTask;

import com.cookpad.puree.outputs.PureeBufferedOutput;

import org.json.JSONObject;

public class AsyncInsertTask extends AsyncTask<Void, Void, Void> {
    private PureeBufferedOutput output;
    private String type;
    private JSONObject jsonLog;

    public AsyncInsertTask(PureeBufferedOutput output, String type, JSONObject jsonLog) {
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
