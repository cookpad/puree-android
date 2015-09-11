package com.cookpad.puree.internal;

import android.util.Log;

import javax.annotation.Nonnull;

public final class PureeVerboseRunnable implements Runnable {
    private static final String TAG = PureeVerboseRunnable.class.getSimpleName();

    private final Runnable task;

    public PureeVerboseRunnable(@Nonnull final Runnable task) {
        this.task = task;
    }

    public void run() {
        try {
            this.task.run();
        } catch (RuntimeException ex) {
            Log.e(TAG, "Puree detected an uncaught runtime exception while executing", ex);
            throw ex;
        } catch (Error error) {
            Log.e(TAG, "Puree detected an uncaught error while executing", error);
            throw error;
        }
    }
}