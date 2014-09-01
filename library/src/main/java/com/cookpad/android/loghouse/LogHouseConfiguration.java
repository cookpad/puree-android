package com.cookpad.android.loghouse;

import android.content.Context;

import com.cookpad.android.loghouse.handlers.BeforeInsertAction;
import com.cookpad.android.loghouse.handlers.BeforeShipAction;
import com.cookpad.android.loghouse.handlers.DeliveryPerson;
import com.cookpad.android.loghouse.async.ShipExecutor;
import com.google.gson.Gson;

import java.util.Calendar;

public class LogHouseConfiguration {
    private Context applicationContext;
    private DeliveryPerson deliveryPerson;
    private Gson gson;
    private int logsPerRequest;
    private int shipIntervalTime;
    private int shipIntervalTimeUnit;
    private BeforeInsertAction beforeInsertAction;
    private BeforeShipAction beforeShipAction;

    public Context getApplicationContext() {
        return applicationContext;
    }

    public DeliveryPerson getDeliveryPerson() {
        return deliveryPerson;
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

    public LogHouseConfiguration(Context applicationContext,
                                 DeliveryPerson deliveryPerson,
                                 Gson gson,
                                 int logsPerRequest,
                                 int shipIntervalTime,
                                 int shipIntervalTimeUnit,
                                 BeforeInsertAction beforeInsertAction,
                                 BeforeShipAction beforeShipAction) {
        this.applicationContext = applicationContext;
        this.deliveryPerson = deliveryPerson;
        this.gson = gson;
        this.logsPerRequest = logsPerRequest;
        this.shipIntervalTime = shipIntervalTime;
        this.shipIntervalTimeUnit = shipIntervalTimeUnit;
        this.beforeInsertAction = beforeInsertAction;
        this.beforeShipAction = beforeShipAction;
    }

    public static class Builder {
        private Context applicationContext;
        private DeliveryPerson deliveryPerson;
        private Gson gson = new Gson();
        private int logsPerRequest = ShipExecutor.DEFAULT_LOGS_PER_REQUEST;
        private BeforeInsertAction beforeInsertAction;
        private BeforeShipAction beforeShipAction;
        private int shipIntervalTime = 5;
        private int shipIntervalTimeUnit = Calendar.MINUTE;

        public Builder(Context applicationContext, DeliveryPerson deliveryPerson) {
            this.applicationContext = applicationContext;
            this.deliveryPerson = deliveryPerson;
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

        public LogHouseConfiguration build() {
            return new LogHouseConfiguration(
                    applicationContext,
                    deliveryPerson,
                    gson,
                    logsPerRequest,
                    shipIntervalTime,
                    shipIntervalTimeUnit,
                    beforeInsertAction,
                    beforeShipAction);
        }
    }
}
