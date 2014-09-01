package com.cookpad.android.loghouse.async;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.cookpad.android.loghouse.LogHouseManager;

import org.json.JSONException;
import org.json.JSONObject;

public class InsertExecutor extends IntentService {
    private static final String EXTRA_SERIALIZED_LOG = "_serialized_log";

    public static void execute(Context context, JSONObject serializedLog) {
        Intent intent = new Intent(context, InsertExecutor.class);
        intent.putExtra(EXTRA_SERIALIZED_LOG, serializedLog.toString());
        context.startService(intent);
    }

    public InsertExecutor() {
        this(InsertExecutor.class.getSimpleName());
    }

    public InsertExecutor(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            JSONObject serializedLog = new JSONObject(intent.getStringExtra(EXTRA_SERIALIZED_LOG));
            LogHouseManager.insertSync(serializedLog);
        } catch (JSONException e) {
            // do nothing
        }
    }
}
