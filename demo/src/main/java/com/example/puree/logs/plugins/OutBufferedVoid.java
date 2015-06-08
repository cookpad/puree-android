package com.example.puree.logs.plugins;

import com.cookpad.puree.async.AsyncResult;
import com.cookpad.puree.outputs.OutputConfiguration;
import com.cookpad.puree.outputs.PureeBufferedOutput;

import org.json.JSONArray;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * PureeBufferedOutput plugin that does nothing. This is for benchmarking.
 */
@ParametersAreNonnullByDefault
public class OutBufferedVoid extends PureeBufferedOutput {

    @Override
    public void emit(JSONArray jsonArray, AsyncResult result) {
        result.success();
    }

    @Nonnull
    @Override
    public String type() {
        return "out_buffered_void";
    }

    @Nonnull
    @Override
    public OutputConfiguration configure(OutputConfiguration conf) {
        conf.setFlushIntervalMillis(10);
        return conf;
    }
}
