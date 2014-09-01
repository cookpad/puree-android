package com.cookpad.android.loghouse.tasks;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.cookpad.android.loghouse.LogHouseManager;

public class ShipExecutor extends IntentService {
    public static final int DEFAULT_LOGS_PER_REQUEST = 1000;

    private static final String EXTRA_LOGS_PER_REQUEST = "_logs_per_request";

    public static void execute(Context context, int logsPerRequest) {
        Intent intent = new Intent(context, ShipExecutor.class);
        intent.putExtra(EXTRA_LOGS_PER_REQUEST, logsPerRequest);
        context.startService(intent);
    }

    public ShipExecutor() {
        this(ShipExecutor.class.getSimpleName());
    }

    public ShipExecutor(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int logsPerRequest = intent.getIntExtra(EXTRA_LOGS_PER_REQUEST, DEFAULT_LOGS_PER_REQUEST);
        LogHouseManager.shipSync(logsPerRequest);
    }
}
