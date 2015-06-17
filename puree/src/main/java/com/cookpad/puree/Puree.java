package com.cookpad.puree;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.cookpad.puree.internal.LogDumper;
import com.cookpad.puree.outputs.PureeOutput;
import com.cookpad.puree.storage.PureeDbHelper;
import com.cookpad.puree.storage.PureeStorage;
import com.cookpad.puree.storage.Records;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class Puree {
    private static final String TAG = Puree.class.getSimpleName();

    private static boolean isInitialized = false;
    private static Gson gson;
    private static PureeStorage storage;

    private static Map<Key, List<PureeOutput>> sourceOutputMap = new HashMap<>();

    public static synchronized void initialize(PureeConfiguration conf) {
        if (isInitialized) {
            Log.w(TAG, "Puree has already been initialized");
            return;
        }

        gson = conf.getGson();
        storage = new PureeDbHelper(conf.getApplicationContext());
        sourceOutputMap = conf.getSourceOutputMap();

        for (List<PureeOutput> outputs : sourceOutputMap.values()) {
            for (PureeOutput output : outputs) {
                output.initialize(storage);
            }
        }

        isInitialized = true;
    }

    /**
     * Serialize a {@link PureeLog} into {@link JsonObject} with {@link Gson}
     */
    public static JsonObject serializeLog(PureeLog log) {
        return (JsonObject) gson.toJsonTree(log);
    }

    @Nullable
    public static List<PureeOutput> getRegisteredOutputPlugins(PureeLog log) {
        return sourceOutputMap.get(Key.from(log.getClass()));
    }


    /**
     * Try to send log. This log is sent immediately or put into buffer (it's depending on output plugin).
     */
    public static void send(PureeLog log) {
        checkIfPureeHasInitialized();

        List<PureeOutput> outputs = getRegisteredOutputPlugins(log);
        if (outputs == null) {
            throw new IllegalStateException("No output plugin registered for " + log);
        }
        for (PureeOutput output : outputs) {
            JsonObject jsonLog = serializeLog(log);
            output.receive(jsonLog);
        }
    }

    /**
     * Try to flush all logs that are in buffer.
     */
    public static void flush() {
        checkIfPureeHasInitialized();
        for (List<PureeOutput> outputs : sourceOutputMap.values()) {
            for (PureeOutput output : outputs) {
                output.flush();
            }
        }
    }

    public static void dump() {
        LogDumper.out(getBufferedLogs());
    }

    /**
     * Get all logs that are in buffer.w
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
            throw new NotInitializedException();
        }
    }

    public static class NotInitializedException extends IllegalStateException {

    }
}
