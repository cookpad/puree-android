package com.cookpad.android.puree;

import android.content.Context;

import com.cookpad.android.puree.outputs.PureeOutput;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PureeConfiguration {
    public static boolean isTest = false;

    private Context applicationContext;
    private Gson gson;
    private List<PureeOutput> outputs = new ArrayList<>();

    public Context getApplicationContext() {
        return applicationContext;
    }

    public Gson getGson() {
        return gson;
    }

    public List<PureeOutput> getOutputs() {
        return outputs;
    }

    public PureeConfiguration(Context applicationContext,
                              Gson gson,
                              List<PureeOutput> outputs) {
        this.applicationContext = applicationContext;
        this.gson = gson;
        this.outputs = outputs;
    }

    public static class Builder {
        private Context applicationContext;
        private Gson gson = new Gson();
        private List<PureeOutput> outputs = new ArrayList<>();

        public Builder(Context applicationContext) {
            this.applicationContext = applicationContext;
        }

        public Builder gson(Gson gson) {
            this.gson = gson;
            return this;
        }

        public Builder registerOutput(PureeOutput output) {
            outputs.add(output);
            return this;
        }

        public Builder registerOutput(PureeOutput output, PureeFilter... filters) {
            for (PureeFilter filter : filters) {
                output.registerFilter(filter);
            }
            outputs.add(output);
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
