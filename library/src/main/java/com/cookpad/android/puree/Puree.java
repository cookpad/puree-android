package com.cookpad.android.puree;

import android.util.Log;

import com.cookpad.android.puree.exceptions.PureeNotInitializedException;
import com.cookpad.android.puree.internal.LogDumper;
import com.cookpad.android.puree.storage.PureeDbHelper;
import com.cookpad.android.puree.storage.PureeStorage;
import com.cookpad.android.puree.storage.Records;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Puree {
    private static final String TAG = Puree.class.getSimpleName();

    private static boolean isInitialized = false;
    private static Gson gson;
    private static PureeStorage storage;
    private static List<PureeOutput> outputs = new ArrayList<>();

    public static synchronized void initialize(PureeConfiguration conf) {
        if (isInitialized) {
            Log.w(TAG, "Puree has already initialized");
        }

        gson = conf.getGson();
        storage = new PureeDbHelper(conf.getApplicationContext());

        for (PureeOutput output : conf.getOutputs()) {
            output.initialize(conf, storage);
            outputs.add(output);
        }

        isInitialized = true;
    }

    public static void in(SerializableLog log) {
        checkIfPureeHasInitialized();
        in(log.type(), log.toJSON(gson));
    }

    private static void in(String type, JSONObject serializedLog) {
        checkIfPureeHasInitialized();
        for (PureeOutput output : outputs) {
            if (output.type().equals(type)) {
                output.start(serializedLog);
            }
        }
    }

    public static Records getBufferedLogs() {
        checkIfPureeHasInitialized();
        return storage.selectAll();
    }

    public static void dump() {
        checkIfPureeHasInitialized();
        LogDumper.outLogcat(storage.selectAll());
    }

    public static void clear() {
        checkIfPureeHasInitialized();
        storage.clear();
    }

    private static void checkIfPureeHasInitialized() {
        if (!isInitialized) {
            throw new PureeNotInitializedException();
        }
    }
}
