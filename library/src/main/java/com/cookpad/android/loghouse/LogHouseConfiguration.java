package com.cookpad.android.loghouse;

import android.content.Context;

import com.cookpad.android.loghouse.handlers.AfterFlushAction;
import com.cookpad.android.loghouse.handlers.BeforeEmitAction;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class LogHouseConfiguration {
    private boolean isTest = false;
    private Context applicationContext;
    private Gson gson;
    private BeforeEmitAction beforeEmitAction;
    private AfterFlushAction afterFlushAction = AfterFlushAction.DEFAULT;
    private List<LogHouse.Output> outputs;

    public boolean isTest() {
        return isTest;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public Gson getGson() {
        return gson;
    }

    public BeforeEmitAction getBeforeEmitAction() {
        return beforeEmitAction;
    }

    AfterFlushAction getAfterFlushAction() {
        return afterFlushAction;
    }

    void setAfterFlushAction(AfterFlushAction afterFlushAction) {
        this.isTest = true;
        this.afterFlushAction = afterFlushAction;
    }

    public List<LogHouse.Output> getOutputs() {
        return outputs;
    }

    public LogHouseConfiguration(Context applicationContext,
                                 Gson gson,
                                 BeforeEmitAction beforeEmitAction,
                                 List<LogHouse.Output> outputs) {
        this.applicationContext = applicationContext;
        this.gson = gson;
        this.beforeEmitAction = beforeEmitAction;
        this.outputs = outputs;
    }

    public static class Builder {
        private Context applicationContext;
        private Gson gson = new Gson();
        private BeforeEmitAction beforeEmitAction = BeforeEmitAction.DEFAULT;
        private List<LogHouse.Output> outputs = new ArrayList<LogHouse.Output>();

        public Builder(Context applicationContext) {
            this.applicationContext = applicationContext;
        }

        public Builder gson(Gson gson) {
            this.gson = gson;
            return this;
        }

        public Builder beforeEmitAction(BeforeEmitAction beforeEmitAction) {
            this.beforeEmitAction = beforeEmitAction;
            return this;
        }

        public Builder registerOutput(LogHouse.Output output) {
            outputs.add(output);
            return this;
        }

        public LogHouseConfiguration build() {
            return new LogHouseConfiguration(
                    applicationContext,
                    gson,
                    beforeEmitAction,
                    outputs);
        }
    }
}
