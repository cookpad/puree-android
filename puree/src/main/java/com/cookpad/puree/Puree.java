package com.cookpad.puree;

import com.cookpad.puree.internal.LogDumper;
import com.cookpad.puree.storage.Records;

import android.util.Log;

import java.util.concurrent.Executor;

public class Puree {

    private static final String TAG = Puree.class.getSimpleName();

    private static PureeLogger logger;

    public static synchronized void initialize(PureeConfiguration conf) {
        if (logger != null) {
            Log.w(TAG, "Puree has already been initialized; re-initialize it with the configuration");
        }
        setPureeLogger(conf.createPureeLogger());
    }

    public static void setPureeLogger(PureeLogger instance) {
        logger = instance;
    }

    /**
     * Try to send log. This log is sent immediately or put into buffer (it's depending on output plugin).
     */
    public static void send(final PureeLog log) {
        checkIfPureeHasInitialized();
        logger.send(log);
    }

    /**
     * Try to flush all logs that are in buffer.
     */
    public static void flush() {
        checkIfPureeHasInitialized();
        logger.flush();
    }

    public static void dump() {
        LogDumper.out(getBufferedLogs());
    }

    /**
     * Get all logs that in buffer.
     */
    public static Records getBufferedLogs() {
        checkIfPureeHasInitialized();
        return logger.getBufferedLogs();
    }

    /**
     * Discards all logs in buffer.
     */
    public static void discardBufferedLogs() {
        checkIfPureeHasInitialized();
        logger.discardBufferedLogs();
    }

    public static Executor getExecutor() {
        return logger.getExecutor();
    }

    private static void checkIfPureeHasInitialized() {
        if (logger == null) {
            throw new NotInitializedException();
        }
    }

    public static class NotInitializedException extends IllegalStateException {

    }
}
