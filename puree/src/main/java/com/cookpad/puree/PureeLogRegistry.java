package com.cookpad.puree;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.cookpad.puree.outputs.PureeOutput;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PureeLogRegistry {

    final Gson gson;

    final Map<Class<?>, List<PureeOutput>> sourceOutputMap = new HashMap<>();

    public PureeLogRegistry(PureeConfiguration conf) {
        sourceOutputMap.putAll(conf.getSourceOutputMap());
        gson = conf.getGson();
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
