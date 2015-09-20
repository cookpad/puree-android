package com.cookpad.puree;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.cookpad.puree.outputs.PureeOutput;
import com.cookpad.puree.storage.PureeStorage;
import com.cookpad.puree.storage.Records;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PureeLogger {

    final Gson gson;

    final Map<Class<?>, List<PureeOutput>> sourceOutputMap = new HashMap<>();

    final PureeStorage storage;

    final ScheduledExecutorService executor;

    public PureeLogger(Map<Class<?>, List<PureeOutput>> sourceOutputMap, Gson gson, PureeStorage storage,
            ScheduledExecutorService executor) {
        this.sourceOutputMap.putAll(sourceOutputMap);
        this.gson = gson;
        this.storage = storage;
        this.executor = executor;

        forEachOutput(new PureeLogger.Consumer<PureeOutput>() {
            @Override
            public void accept(@Nonnull PureeOutput value) {
                value.initialize(PureeLogger.this);
            }
        });
    }

    public void send(PureeLog log) {
        List<PureeOutput> outputs = getRegisteredOutputPlugins(log);
        for (PureeOutput output : outputs) {
            JsonObject jsonLog = serializeLog(log);
            output.receive(jsonLog);
        }
    }

    public PureeStorage getStorage() {
        return storage;
    }

    public ScheduledExecutorService getExecutor() {
        return executor;
    }

    public Records getBufferedLogs() {
        return storage.selectAll();
    }

    public void discardBufferedLogs() {
        storage.clear();
    }

    public void truncateBufferedLogs(int truncateThresholdInRows) {
        storage.truncateBufferedLogs(truncateThresholdInRows);
    }

    public void flush() {
        forEachOutput(new PureeLogger.Consumer<PureeOutput>() {
            @Override
            public void accept(@Nonnull PureeOutput value) {
                value.flush();
            }
        });
    }

    /**
     * Serialize a {@link PureeLog} into {@link JsonObject} with {@link Gson}.
     *
     * @param log {@link PureeLog}.
     * @return serialized json object.
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
        for (List<PureeOutput> outputs : new HashSet<>(sourceOutputMap.values())) {
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
