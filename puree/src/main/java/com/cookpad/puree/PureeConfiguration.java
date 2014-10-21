package com.cookpad.puree;

import android.content.Context;

import com.cookpad.puree.outputs.PureeOutput;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class PureeConfiguration {
    public static boolean isTest = false;

    private Context applicationContext;
    private Gson gson;
    private Map<String, PureeOutput> outputs = new HashMap<>();

    public Context getApplicationContext() {
        return applicationContext;
    }

    public Gson getGson() {
        return gson;
    }

    public Map<String, PureeOutput> getOutputs() {
        return outputs;
    }

    public PureeConfiguration(Context applicationContext,
                              Gson gson,
                              Map<String, PureeOutput> outputs) {
        this.applicationContext = applicationContext;
        this.gson = gson;
        this.outputs = outputs;
    }

    public static class Builder {
        private Context applicationContext;
        private Gson gson = new Gson();
        private Map<String, PureeOutput> outputs = new HashMap<>();

        public Builder(Context applicationContext) {
            this.applicationContext = applicationContext;
        }

        public Builder gson(Gson gson) {
            this.gson = gson;
            return this;
        }

        public Builder registerOutput(PureeOutput output, PureeFilter... filters) {
            for (PureeFilter filter : filters) {
                output.registerFilter(filter);
            }
            outputs.put(output.type(), output);
            return this;
        }

        public PureeConfiguration build() {
            return new PureeConfiguration(
                    applicationContext,
                    gson,
                    outputs);
        }
    }
}
