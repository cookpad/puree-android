package com.cookpad.android.puree;

import android.content.Context;

import com.cookpad.android.puree.handlers.AfterFlushFilter;
import com.cookpad.android.puree.handlers.BeforeEmitFilter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PureeConfiguration {
    public static boolean isTest = false;

    private Context applicationContext;
    private Gson gson;
    private BeforeEmitFilter beforeEmitFilter;
    private AfterFlushFilter afterFlushFilter = AfterFlushFilter.DEFAULT;
    private List<PureeOutput> outputs = new ArrayList<>();

    public Context getApplicationContext() {
        return applicationContext;
    }

    public Gson getGson() {
        return gson;
    }

    public BeforeEmitFilter getBeforeEmitFilter() {
        return beforeEmitFilter;
    }

    AfterFlushFilter getAfterFlushFilter() {
        return afterFlushFilter;
    }

    void setAfterFlushFilter(AfterFlushFilter afterFlushFilter) {
        isTest = true;
        this.afterFlushFilter = afterFlushFilter;
    }

    public List<PureeOutput> getOutputs() {
        return outputs;
    }

    public PureeConfiguration(Context applicationContext,
                              Gson gson,
                              BeforeEmitFilter beforeEmitFilter,
                              List<PureeOutput> outputs) {
        this.applicationContext = applicationContext;
        this.gson = gson;
        this.beforeEmitFilter = beforeEmitFilter;
        this.outputs = outputs;
    }

    public static class Builder {
        private Context applicationContext;
        private Gson gson = new Gson();
        private BeforeEmitFilter beforeEmitFilter = BeforeEmitFilter.DEFAULT;
        private List<PureeOutput> outputs = new ArrayList<>();

        public Builder(Context applicationContext) {
            this.applicationContext = applicationContext;
        }

        public Builder gson(Gson gson) {
            this.gson = gson;
            return this;
        }

        public Builder beforeEmitAction(BeforeEmitFilter beforeEmitFilter) {
            this.beforeEmitFilter = beforeEmitFilter;
            return this;
        }

        public Builder registerOutput(PureeOutput output) {
            outputs.add(output);
            return this;
        }

        public PureeConfiguration build() {
            return new PureeConfiguration(
                    applicationContext,
                    gson,
                    beforeEmitFilter,
                    outputs);
        }
    }
}
