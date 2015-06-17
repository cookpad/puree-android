package com.cookpad.puree;

import com.google.gson.JsonObject;

import com.cookpad.puree.internal.LogDumper;
import com.cookpad.puree.outputs.PureeOutput;
import com.cookpad.puree.storage.PureeDbHelper;
import com.cookpad.puree.storage.PureeStorage;
import com.cookpad.puree.storage.Records;

import android.util.Log;

import java.util.List;

import javax.annotation.Nonnull;

public class Puree {
    private static final String TAG = Puree.class.getSimpleName();

    private static boolean isInitialized = false;
    private static PureeStorage storage;

    private static PureeLogRegistry registry;

    public static synchronized void initialize(PureeConfiguration conf) {
        if (isInitialized) {
            Log.w(TAG, "Puree has already been initialized");
            return;
        }

        storage = new PureeDbHelper(conf.getApplicationContext());
        registry = new PureeLogRegistry(conf);

        registry.forEachOutput(new PureeLogRegistry.Consumer<PureeOutput>() {
            @Override
            public void accept(@Nonnull PureeOutput value) {
                value.initialize(storage);
            }
        });

        isInitialized = true;
    }

    /**
     * Try to send log. This log is sent immediately or put into buffer (it's depending on output plugin).
     */
    public static void send(PureeLog log) {
        checkIfPureeHasInitialized();

        List<PureeOutput> outputs = registry.getRegisteredOutputPlugins(log);
        for (PureeOutput output : outputs) {
            JsonObject jsonLog = registry.serializeLog(log);
            output.receive(jsonLog);
        }
    }

    /**
     * Try to flush all logs that are in buffer.
     */
    public static void flush() {
        checkIfPureeHasInitialized();

        registry.forEachOutput(new PureeLogRegistry.Consumer<PureeOutput>() {
            @Override
            public void accept(@Nonnull PureeOutput value) {
                value.flush();
            }
        });
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
