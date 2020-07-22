package com.cookpad.puree;

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
    private final PureeSerializer pureeSerializer;

    final Map<Class<?>, List<PureeOutput>> sourceOutputMap = new HashMap<>();

    final PureeStorage storage;

    final ScheduledExecutorService executor;

    public PureeLogger(Map<Class<?>, List<PureeOutput>> sourceOutputMap, PureeSerializer pureeSerializer, PureeStorage storage,
            ScheduledExecutorService executor) {
        this.sourceOutputMap.putAll(sourceOutputMap);
        this.pureeSerializer = pureeSerializer;
        this.storage = storage;
        this.executor = executor;

        forEachOutput(new PureeLogger.Consumer<PureeOutput>() {
            @Override
            public void accept(@Nonnull PureeOutput value) {
                value.initialize(PureeLogger.this);
            }
        });
    }

    public void send(Object log) {
        List<PureeOutput> outputs = getRegisteredOutputPlugins(log);
        for (PureeOutput output : outputs) {
            output.receive(serializeLog(log));
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
     * Serialize a {@link Object} into a json string representation.
     *
     * @param log {@link Object}.
     * @return serialized json object.
     */
    @Nonnull
    public String serializeLog(Object log) {
        return pureeSerializer.serialize(log);
    }

    @Nonnull
    public List<PureeOutput> getRegisteredOutputPlugins(Object log) {
        return getRegisteredOutputPlugins(log.getClass());
    }

    @Nonnull
    public List<PureeOutput> getRegisteredOutputPlugins(Class<?> logClass) {
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
