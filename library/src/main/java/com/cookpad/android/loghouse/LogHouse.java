package com.cookpad.android.loghouse;

import android.util.Log;

import com.cookpad.android.loghouse.exceptions.LogHouseNotInitializedException;
import com.cookpad.android.loghouse.internal.LogDumper;
import com.cookpad.android.loghouse.storage.LogHouseDbHelper;
import com.cookpad.android.loghouse.storage.LogHouseStorage;
import com.cookpad.android.loghouse.storage.Records;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LogHouse {
    private static final String TAG = LogHouse.class.getSimpleName();

    private static boolean isInitialized = false;
    private static Gson gson;
    private static LogHouseStorage storage;
    private static List<LogHouseOutput> outputs = new ArrayList<>();

    public static synchronized void initialize(LogHouseConfiguration conf) {
        if (isInitialized) {
            Log.w(TAG, "LogHouse has already initialized");
        }

        gson = conf.getGson();
        storage = new LogHouseDbHelper(conf.getApplicationContext());

        for (LogHouseOutput output : conf.getOutputs()) {
            output.initialize(conf, storage);
            outputs.add(output);
        }

        isInitialized = true;
    }

    public static void in(SerializableLog log) {
        checkIfLogHouseHasInitialized();
        in(log.type(), log.toJSON(gson));
    }

    private static void in(String type, JSONObject serializedLog) {
        checkIfLogHouseHasInitialized();
        for (LogHouseOutput output : outputs) {
            if (output.type().equals(type)) {
                output.start(serializedLog);
            }
        }
    }

    public static Records getBufferedLogs() {
        checkIfLogHouseHasInitialized();
        return storage.selectAll();
    }

    public static void dump() {
        checkIfLogHouseHasInitialized();
        LogDumper.outLogcat(storage.selectAll());
    }

    public static void clear() {
        checkIfLogHouseHasInitialized();
        storage.clear();
    }

    private static void checkIfLogHouseHasInitialized() {
        if (!isInitialized) {
            throw new LogHouseNotInitializedException();
        }
    }
}
