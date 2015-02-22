package com.cookpad.puree;

import android.content.Context;
import android.util.Log;

import com.cookpad.puree.outputs.PureeOutput;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PureeConfiguration {
    private static final String TAG = PureeConfiguration.class.getSimpleName();

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

    PureeConfiguration(Context context, Gson gson, Map<Key, List<PureeOutput>> sourceOutputMap) {
        this.applicationContext = context.getApplicationContext();
        this.gson = gson;
        this.sourceOutputMap = sourceOutputMap;
    }

    /** Print mapping of SOURCE -> FILTER... OUTPUT. */
    public void printMapping() {
        Log.i(TAG, "# SOURCE -> FILTER... -> OUTPUT");
        for (Key key : sourceOutputMap.keySet()) {
            StringBuilder builder;
            for (PureeOutput output : sourceOutputMap.get(key)) {
                builder = new StringBuilder(key.getId());
                for (PureeFilter filter : output.getFilters()) {
                    builder.append(" -> ").append(filter.getClass().getSimpleName());
                }
                builder.append(" -> ").append(output.getClass().getSimpleName());
                Log.i(TAG, builder.toString());
            }
        }
    }

    public static class Builder {
        private Context context;
        private Gson gson = new Gson();
        private Map<Key, List<PureeOutput>> sourceOutputMap = new HashMap<>();

        /** Start building a new {@link com.cookpad.puree.PureeConfiguration} instance. */
        public Builder(Context context) {
            this.context = context;
        }

        /** Specify the {@link com.google.gson.Gson} to serialize logs. */
        public Builder gson(Gson gson) {
            this.gson = gson;
            return this;
        }

        /** Specify the source class of log. */
        public Source source(Class<? extends JsonConvertible> clazz) {
            Key key = Key.from(clazz);
            return new Source(this, key);
        }

        void registerOutput(Key key, PureeOutput output, List<PureeFilter> filters) {
            if (filters != null) {
                for (PureeFilter filter : filters) {
                    output.registerFilter(filter);
                }
            }

            List<PureeOutput> outputs = sourceOutputMap.get(key);
            if (outputs == null) {
                outputs = new ArrayList<>();
            }
            outputs.add(output);
            sourceOutputMap.put(key, outputs);
        }

        /** Create the {@link com.cookpad.puree.PureeConfiguration} instance. */
        public PureeConfiguration build() {
            return new PureeConfiguration(context, gson, sourceOutputMap);
        }
    }
}
