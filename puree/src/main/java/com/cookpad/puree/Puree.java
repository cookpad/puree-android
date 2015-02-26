package com.cookpad.puree;

import android.util.Log;

import com.cookpad.puree.exceptions.PureeNotInitializedException;
import com.cookpad.puree.internal.LogDumper;
import com.cookpad.puree.outputs.PureeOutput;
import com.cookpad.puree.storage.PureeDbHelper;
import com.cookpad.puree.storage.PureeStorage;
import com.cookpad.puree.storage.Records;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Puree {
    private static final String TAG = Puree.class.getSimpleName();

    private static boolean isInitialized = false;
    private static Gson gson;
    private static PureeStorage storage;
    private static Map<Key, List<PureeOutput>> sourceOutputMap = new HashMap<>();

    public static synchronized void initialize(PureeConfiguration conf) {
        if (isInitialized) {
            Log.w(TAG, "Puree has already initialized");
            return;
        }

        gson = conf.getGson();
        storage = new PureeDbHelper(conf.getApplicationContext());
        sourceOutputMap = conf.getSourceOutputMap();

        for (Key key : sourceOutputMap.keySet()) {
            List<PureeOutput> outputs = sourceOutputMap.get(key);
            for (PureeOutput output : outputs) {
                output.initialize(storage);
            }
        }

        isInitialized = true;
    }

    /**
     * Try to send log. This log is sent immediately or put into buffer (it's depending on output plugin).
     */
    public static void send(JsonConvertible log) {
        checkIfPureeHasInitialized();

        Key key = Key.from(log.getClass());
        List<PureeOutput> outputs = sourceOutputMap.get(key);
        for (PureeOutput output : outputs) {
            output.receive(log.toJson(gson));
        }
    }

    /**
     * Try to flush all logs that are in buffer.
     */
    public static void flush() {
        checkIfPureeHasInitialized();
        for (Key key : sourceOutputMap.keySet()) {
            List<PureeOutput> outputs = sourceOutputMap.get(key);
            for (PureeOutput output : outputs) {
                output.flush();
            }
        }
    }

    public static void dump() {
        LogDumper.out(getBufferedLogs());
    }

    /**
     * Get all logs that are in buffer.
     */
    public static Records getBufferedLogs() {
        checkIfPureeHasInitialized();
        return storage.selectAll();
    }

    /**
     * Delete all logs that are in buffer.
     */
    public static void clear() {
        checkIfPureeHasInitialized();
        storage.clear();
    }

    private static synchronized void checkIfPureeHasInitialized() {
        if (!isInitialized) {
            throw new PureeNotInitializedException();
        }
    }
}
