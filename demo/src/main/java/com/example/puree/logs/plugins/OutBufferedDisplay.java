package com.example.puree.logs.plugins;

import com.cookpad.puree.async.AsyncResult;
import com.cookpad.puree.outputs.OutputConfiguration;
import com.cookpad.puree.outputs.PureeBufferedOutput;

import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class OutBufferedDisplay extends PureeBufferedOutput {
    private static WeakReference<Callback> callbackRef = new WeakReference<>(null);

    public static void register(Callback callback) {
        callbackRef = new WeakReference<>(callback);
    }

    public static void unregister() {
        callbackRef.clear();
    }

    @Override
    public String type() {
        return "out_buffered_display";
    }

    @Nonnull
    @Override
    public OutputConfiguration configure(OutputConfiguration conf) {
        conf.setFlushIntervalMillis(3000);
        return conf;
    }

    @Override
    public void emit(final List<String> jsonLogs, final AsyncResult result) {
        final Callback callback = callbackRef.get();
        if (callback == null) {
            result.success();
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                callback.onEmit(jsonLogs);
                result.success();
            }
        });
    }

    public interface Callback {

        void onEmit(List<String> jsonLogs);
    }
}
