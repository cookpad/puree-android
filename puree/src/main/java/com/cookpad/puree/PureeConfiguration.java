package com.cookpad.puree;

import android.content.Context;

import com.cookpad.puree.outputs.PureeOutput;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PureeConfiguration {
    private Context applicationContext;
    private Gson gson;
    private Map<Key, List<PureeOutput>> sourceOutputMap;

    public Context getApplicationContext() {
        return applicationContext;
    }

    public Gson getGson() {
        return gson;
    }

    public Map<Key, List<PureeOutput>> getSourceOutputMap() {
        return sourceOutputMap;
    }

    PureeConfiguration(Context context,
                       Gson gson,
                       Map<Key, List<PureeOutput>> sourceOutputMap) {
        this.applicationContext = context.getApplicationContext();
        this.gson = gson;
        this.sourceOutputMap = sourceOutputMap;
    }

    public static class Builder {
        private Context context;
        private Gson gson = new Gson();
        private Map<Key, List<PureeOutput>> sourceOutputMap = new HashMap<>();

        public Builder(Context context) {
            this.context = context;
        }

        public Builder gson(Gson gson) {
            this.gson = gson;
            return this;
        }

        public Source source(Class<?> clazz) {
            Key key = Key.from(clazz);
            return new Source(this, key);
        }

        void registerOutput(Key key, PureeOutput output, PureeFilter filter) {
            if (filter != null) {
                output.registerFilter(filter);
            }

            List<PureeOutput> outputs = sourceOutputMap.get(key);
            if (outputs == null) {
                outputs = new ArrayList<>();
            }
            outputs.add(output);
            sourceOutputMap.put(key, outputs);
        }

        public PureeConfiguration build() {
            return new PureeConfiguration(
                    context,
                    gson,
                    sourceOutputMap);
        }
    }
}
