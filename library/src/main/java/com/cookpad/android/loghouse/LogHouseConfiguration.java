package com.cookpad.android.loghouse;

import android.content.Context;

import com.cookpad.android.loghouse.handlers.AfterShipAction;
import com.cookpad.android.loghouse.handlers.BeforeInsertAction;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class LogHouseConfiguration {
    private Context applicationContext;
    private Gson gson;
    private BeforeInsertAction beforeInsertAction;
    private AfterShipAction afterShipAction;
    private List<LogHouse.Output> outputs;

    public Context getApplicationContext() {
        return applicationContext;
    }

    public Gson getGson() {
        return gson;
    }

    public BeforeInsertAction getBeforeInsertAction() {
        return beforeInsertAction;
    }

    public AfterShipAction getAfterShipAction() {
        return afterShipAction;
    }

    public List<LogHouse.Output> getOutputs() {
        return outputs;
    }

    public LogHouseConfiguration(Context applicationContext,
                                 Gson gson,
                                 BeforeInsertAction beforeInsertAction,
                                 AfterShipAction afterShipAction,
                                 List<LogHouse.Output> outputs) {
        this.applicationContext = applicationContext;
        this.gson = gson;
        this.beforeInsertAction = beforeInsertAction;
        this.afterShipAction = afterShipAction;
        this.outputs = outputs;
    }

    public static class Builder {
        private Context applicationContext;
        private Gson gson = new Gson();
        private BeforeInsertAction beforeInsertAction = BeforeInsertAction.DEFAULT;
        private AfterShipAction afterShipAction = AfterShipAction.DEFAULT;
        private List<LogHouse.Output> outputs = new ArrayList<LogHouse.Output>();

        public Builder(Context applicationContext) {
            this.applicationContext = applicationContext;
        }

        public Builder gson(Gson gson) {
            this.gson = gson;
            return this;
        }

        public Builder beforeInsertAction(BeforeInsertAction beforeInsertAction) {
            this.beforeInsertAction = beforeInsertAction;
            return this;
        }

        public Builder afterShipAction(AfterShipAction afterShipAction) {
            this.afterShipAction = afterShipAction;
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
                    beforeInsertAction,
                    afterShipAction,
                    outputs);
        }
    }
}
