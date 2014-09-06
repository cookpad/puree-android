package com.cookpad.android.loghouse;

import android.content.Context;

import com.cookpad.android.loghouse.handlers.AfterShipAction;
import com.cookpad.android.loghouse.handlers.BeforeInsertAction;
import com.cookpad.android.loghouse.handlers.BeforeShipAction;
import com.cookpad.android.loghouse.async.ShipExecutor;
import com.cookpad.android.loghouse.plugins.OutLogcat;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LogHouseConfiguration {
    private Context applicationContext;
    private Gson gson;
    private int logsPerRequest;
    private int shipIntervalTime;
    private int shipIntervalTimeUnit;
    private BeforeInsertAction beforeInsertAction;
    private BeforeShipAction beforeShipAction;
    private AfterShipAction afterShipAction;
    private List<LogHouseOutput> outputs = new ArrayList<LogHouseOutput>() {{
        add(new OutLogcat());
    }};

    public Context getApplicationContext() {
        return applicationContext;
    }

    public Gson getGson() {
        return gson;
    }

    public int getLogsPerRequest() {
        return logsPerRequest;
    }

    public int getShipIntervalTime() {
        return shipIntervalTime;
    }

    public int getShipIntervalTimeUnit() {
        return shipIntervalTimeUnit;
    }

    public BeforeInsertAction getBeforeInsertAction() {
        return beforeInsertAction;
    }

    public BeforeShipAction getBeforeShipAction() {
        return beforeShipAction;
    }

    public AfterShipAction getAfterShipAction() {
        return afterShipAction;
    }

    public List<LogHouseOutput> getOutputs() {
        return outputs;
    }

    public LogHouseConfiguration(Context applicationContext,
                                 Gson gson,
                                 int logsPerRequest,
                                 int shipIntervalTime,
                                 int shipIntervalTimeUnit,
                                 BeforeInsertAction beforeInsertAction,
                                 BeforeShipAction beforeShipAction,
                                 AfterShipAction afterShipAction) {
        this.applicationContext = applicationContext;
        this.gson = gson;
        this.logsPerRequest = logsPerRequest;
        this.shipIntervalTime = shipIntervalTime;
        this.shipIntervalTimeUnit = shipIntervalTimeUnit;
        this.beforeInsertAction = beforeInsertAction;
        this.beforeShipAction = beforeShipAction;
        this.afterShipAction = afterShipAction;
    }

    public static class Builder {
        private Context applicationContext;
        private Gson gson = new Gson();
        private int logsPerRequest = ShipExecutor.DEFAULT_LOGS_PER_REQUEST;
        private int shipIntervalTime = 5;
        private int shipIntervalTimeUnit = Calendar.MINUTE;
        private BeforeInsertAction beforeInsertAction = BeforeInsertAction.DEFAULT;
        private BeforeShipAction beforeShipAction = BeforeShipAction.DEFAULT;
        private AfterShipAction afterShipAction = AfterShipAction.DEFAULT;

        public Builder(Context applicationContext) {
            this.applicationContext = applicationContext;
        }

        public Builder gson(Gson gson) {
            this.gson = gson;
            return this;
        }

        public Builder logsPerRequest(int logsPerRequest) {
            this.logsPerRequest = logsPerRequest;
            return this;
        }

        public Builder shipInterval(int shipIntervalTime, int shipIntervalTimeUnit) {
            this.shipIntervalTime = shipIntervalTime;
            this.shipIntervalTimeUnit = shipIntervalTimeUnit;
            return this;
        }

        public Builder beforeInsertAction(BeforeInsertAction beforeInsertAction) {
            this.beforeInsertAction = beforeInsertAction;
            return this;
        }

        public Builder beforeShipAction(BeforeShipAction beforeShipAction) {
            this.beforeShipAction = beforeShipAction;
            return this;
        }

        public Builder afterShipAction(AfterShipAction afterShipAction) {
            this.afterShipAction = afterShipAction;
            return this;
        }

        public LogHouseConfiguration build() {
            return new LogHouseConfiguration(
                    applicationContext,
                    gson,
                    logsPerRequest,
                    shipIntervalTime,
                    shipIntervalTimeUnit,
                    beforeInsertAction,
                    beforeShipAction,
                    afterShipAction);
        }
    }
}
