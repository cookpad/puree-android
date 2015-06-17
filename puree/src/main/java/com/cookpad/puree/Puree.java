package com.cookpad.puree;

import com.google.gson.JsonObject;

import com.cookpad.puree.internal.LogDumper;
import com.cookpad.puree.outputs.PureeOutput;
import com.cookpad.puree.storage.Records;

import android.util.Log;

import java.util.List;

public class Puree {

    private static final String TAG = Puree.class.getSimpleName();

    private static PureeLogRegistry registry;

    public static synchronized void initialize(PureeConfiguration conf) {
        if (registry != null) {
            Log.w(TAG, "Puree has already been initialized; re-initialize it with the configuration");
        }

        registry = conf.createPureeLogRegistry();
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
        registry.flush();
    }

    public static void dump() {
        LogDumper.out(getBufferedLogs());
    }

    /**
     * Get all logs that in buffer.
     */
    public static Records getBufferedLogs() {
        checkIfPureeHasInitialized();
        return registry.getBufferedLogs();
    }

    /**
     * Discards all logs in buffer.
     */
    public static void discardBufferedLogs() {
        checkIfPureeHasInitialized();
        registry.discardBufferedLogs();
    }

    private static synchronized void checkIfPureeHasInitialized() {
        if (registry == null) {
            throw new NotInitializedException();
        }
    }

    public static class NotInitializedException extends IllegalStateException {

    }
}
