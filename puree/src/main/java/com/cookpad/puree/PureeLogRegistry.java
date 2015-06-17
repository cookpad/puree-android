package com.cookpad.puree;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.cookpad.puree.outputs.PureeOutput;
import com.cookpad.puree.storage.PureeStorage;
import com.cookpad.puree.storage.Records;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PureeLogRegistry {

    final Gson gson;

    final Map<Class<?>, List<PureeOutput>> sourceOutputMap = new HashMap<>();

    final PureeStorage storage;

    public PureeLogRegistry(Map<Class<?>, List<PureeOutput>> sourceOutputMap, Gson gson, PureeStorage storage) {
        this.sourceOutputMap.putAll(sourceOutputMap);
        this.gson = gson;
        this.storage = storage;

        forEachOutput(new PureeLogRegistry.Consumer<PureeOutput>() {
            @Override
            public void accept(@Nonnull PureeOutput value) {
                value.initialize(PureeLogRegistry.this.storage);
            }
        });

    }

    public Records getBufferedLogs() {
        return storage.selectAll();
    }

    public void discardBufferedLogs() {
        storage.clear();
    }

    public void flush() {
        forEachOutput(new PureeLogRegistry.Consumer<PureeOutput>() {
            @Override
            public void accept(@Nonnull PureeOutput value) {
                value.flush();
            }
        });
    }

    /**
     * Serialize a {@link PureeLog} into {@link JsonObject} with {@link Gson}
     */
    @Nonnull
    public JsonObject serializeLog(PureeLog log) {
        return (JsonObject) gson.toJsonTree(log);
    }

    @Nonnull
    public List<PureeOutput> getRegisteredOutputPlugins(PureeLog log) {
        return getRegisteredOutputPlugins(log.getClass());
    }

    @Nonnull
    public List<PureeOutput> getRegisteredOutputPlugins(Class<? extends PureeLog> logClass) {
        List<PureeOutput> outputs = sourceOutputMap.get(logClass);
        if (outputs == null) {
            throw new NoRegisteredOutputPluginException("No output plugin registered for " + logClass);
        }
        return outputs;
    }

    public interface Consumer<T> {

        void accept(@Nonnull T value);
    }

    public void forEachOutput(Consumer<PureeOutput> f) {
        for (List<PureeOutput> outputs : sourceOutputMap.values()) {
            for (PureeOutput output : outputs) {
                f.accept(output);
            }
        }
    }

    public static class NoRegisteredOutputPluginException extends IllegalStateException {

        public NoRegisteredOutputPluginException(String detailMessage) {
            super(detailMessage);
        }
    }
}
