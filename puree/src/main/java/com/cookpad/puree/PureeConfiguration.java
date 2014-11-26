package com.cookpad.puree;

import android.content.Context;

import com.cookpad.puree.outputs.PureeOutput;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class PureeConfiguration {
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

    public PureeConfiguration(Context context,
                              Gson gson,
                              Map<String, PureeOutput> outputs) {
        this.applicationContext = context.getApplicationContext();
        this.gson = gson;
        this.outputs = outputs;
    }

    public static class Builder {
        private Context context;
        private Gson gson = new Gson();
        private Map<String, PureeOutput> outputs = new HashMap<>();

        public Builder(Context context) {
            this.context = context;
        }

        public Builder gson(Gson gson) {
            this.gson = gson;
            return this;
        }

        public Builder registerOutput(PureeOutput output, PureeFilter... filters) {
            for (PureeFilter filter : filters) {
                output.registerFilter(filter);
            }
            if (outputs.put(output.type(), output) != null) {
                throw new IllegalStateException("duplicate PureeOutput for type: " + output.type());
            }
            return this;
        }

        public PureeConfiguration build() {
            return new PureeConfiguration(
                    context,
                    gson,
                    outputs);
        }
    }
}
