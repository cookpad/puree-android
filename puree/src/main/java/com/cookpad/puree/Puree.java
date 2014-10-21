package com.cookpad.puree;

import android.util.Log;

import com.cookpad.puree.exceptions.PureeNotInitializedException;
import com.cookpad.puree.internal.LogDumper;
import com.cookpad.puree.outputs.OutputMatcher;
import com.cookpad.puree.outputs.PureeOutput;
import com.cookpad.puree.storage.PureeDbHelper;
import com.cookpad.puree.storage.PureeStorage;
import com.cookpad.puree.storage.Records;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Puree {
    private static final String TAG = Puree.class.getSimpleName();

    private static boolean isInitialized = false;
    private static Gson gson;
    private static PureeStorage storage;
    private static Map<String, PureeOutput> outputs = new HashMap<>();

    public static synchronized void initialize(PureeConfiguration conf) {
        if (isInitialized && !PureeConfiguration.isTest) {
            Log.w(TAG, "Puree has already initialized");
            return;
        }

        gson = conf.getGson();
        storage = new PureeDbHelper(conf.getApplicationContext());
        outputs = conf.getOutputs();

        for (String type : outputs.keySet()) {
            outputs.get(type).initialize(storage);
        }

        isInitialized = true;
    }

    public static void send(JsonConvertible log, String... sendTo) {
        checkIfPureeHasInitialized();

        for (PureeOutput output : OutputMatcher.matchWith(outputs, sendTo)) {
            output.receive(log.toJSON(gson));
        }
    }

    public static void dump() {
        LogDumper.outLogcat(getBufferedLogs());
    }

    public static Records getBufferedLogs() {
        checkIfPureeHasInitialized();
        return storage.selectAll();
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
