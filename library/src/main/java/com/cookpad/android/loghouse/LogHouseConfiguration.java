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
    private List<Class<? extends LogHouse.Output>> outputTypes = new ArrayList<>();

    public void isTest(boolean isTest) {
        this.isTest = isTest;
    }

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

    public List<Class<? extends LogHouse.Output>> getOutputTypes() {
        return outputTypes;
    }

    public LogHouseConfiguration(Context applicationContext,
                                 Gson gson,
                                 BeforeEmitAction beforeEmitAction,
                                 List<Class<? extends LogHouse.Output>> outputTypes) {
        this.applicationContext = applicationContext;
        this.gson = gson;
        this.beforeEmitAction = beforeEmitAction;
        this.outputTypes = outputTypes;
    }

    public static class Builder {
        private Context applicationContext;
        private Gson gson = new Gson();
        private BeforeEmitAction beforeEmitAction = BeforeEmitAction.DEFAULT;
        private List<Class<? extends LogHouse.Output>> outputTypes = new ArrayList<>();

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

        public Builder registerOutput(Class<? extends LogHouse.Output> outputType) {
            outputTypes.add(outputType);
            return this;
        }

        public LogHouseConfiguration build() {
            return new LogHouseConfiguration(
                    applicationContext,
                    gson,
                    beforeEmitAction,
                    outputTypes);
        }
    }
}
