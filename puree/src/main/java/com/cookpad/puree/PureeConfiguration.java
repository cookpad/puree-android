package com.cookpad.puree;

import com.cookpad.puree.internal.LogDumper;
import com.cookpad.puree.outputs.PureeOutput;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PureeConfiguration {

    private final Context applicationContext;

    private final JsonStringifier jsonStringifier;

    private final Map<Key, List<PureeOutput>> sourceOutputMap;

    public Context getApplicationContext() {
        return applicationContext;
    }

    public JsonStringifier getJsonStringifier() {
        return jsonStringifier;
    }

    public Map<Key, List<PureeOutput>> getSourceOutputMap() {
        return sourceOutputMap;
    }

    PureeConfiguration(Context context, JsonStringifier jsonStringifier, Map<Key, List<PureeOutput>> sourceOutputMap) {
        this.applicationContext = context.getApplicationContext();
        this.jsonStringifier = jsonStringifier;
        this.sourceOutputMap = sourceOutputMap;
    }

    /**
     * Print mapping of SOURCE -> FILTER... OUTPUT.
     */
    public void printMapping() {
        LogDumper.out(sourceOutputMap);
    }

    public static class Builder {
        private Context context;

        private JsonStringifier jsonStringifier;

        private Map<Key, List<PureeOutput>> sourceOutputMap = new HashMap<>();

        /**
         * Start building a new {@link com.cookpad.puree.PureeConfiguration} instance.
         */
        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Specify the {@link com.cookpad.puree.JsonStringifier} to serialize logs.
         */
        public Builder jsonStringifier(JsonStringifier jsonStringifier) {
            this.jsonStringifier = jsonStringifier;
            return this;
        }

        /**
         * Specify a source class of logs, which returns {@link Source} an
         * {@link Source#to(PureeOutput)} must be called to register an output plugin.
         */
        public Source source(Class<? extends JsonConvertible> logClass) {
            return new Source(this, Key.from(logClass));
        }

        /**
         * @return the builder itself
         */
        public Builder register(Class<? extends JsonConvertible> logClass, PureeOutput output) {
            return register(Key.from(logClass), output);
        }

        Builder register(Key key, PureeOutput output) {
            List<PureeOutput> outputs = sourceOutputMap.get(key);
            if (outputs == null) {
                outputs = new ArrayList<>();
            }
            outputs.add(output);
            sourceOutputMap.put(key, outputs);
            return this;
        }

        /**
         * Create the {@link com.cookpad.puree.PureeConfiguration} instance.
         */
        public PureeConfiguration build() {
            return new PureeConfiguration(context, jsonStringifier, sourceOutputMap);
        }
    }
}
