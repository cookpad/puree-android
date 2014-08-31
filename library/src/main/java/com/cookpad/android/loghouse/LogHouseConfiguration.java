package com.cookpad.android.loghouse;

import android.content.Context;

import com.cookpad.android.loghouse.handlers.BeforeShipFilter;
import com.cookpad.android.loghouse.handlers.DeliveryPerson;
import com.google.gson.Gson;

import java.util.Calendar;

public class LogHouseConfiguration {
    private Context applicationContext;
    private DeliveryPerson deliveryPerson;
    private Gson gson;
    private int logsPerRequest;
    private int shipIntervalTime;
    private int shipIntervalTimeUnit;
    private BeforeShipFilter beforeShipFilter;

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

    public BeforeShipFilter getBeforeShipFilter() {
        return beforeShipFilter;
    }

    public LogHouseConfiguration(Context applicationContext, DeliveryPerson deliveryPerson,
                                 Gson gson, int logsPerRequest, BeforeShipFilter beforeShipFilter,
                                 int shipIntervalTime, int shipIntervalTimeUnit) {
        this.applicationContext = applicationContext;
        this.deliveryPerson = deliveryPerson;
        this.gson = gson;
        this.logsPerRequest = logsPerRequest;
        this.shipIntervalTime = shipIntervalTime;
        this.shipIntervalTimeUnit = shipIntervalTimeUnit;
        this.beforeShipFilter = beforeShipFilter;
    }

    public static class Builder {
        private Context applicationContext;
        private DeliveryPerson deliveryPerson;
        private Gson gson = new Gson();
        private int logsPerRequest = 1000;
        private BeforeShipFilter beforeShipFilter = new BeforeShipFilter();
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

        public Builder beforeShipFilter(BeforeShipFilter beforeShipFilter) {
            this.beforeShipFilter = beforeShipFilter;
            return this;
        }

        public Builder shipInterval(int shipIntervalTime, int shipIntervalTimeUnit) {
            this.shipIntervalTime = shipIntervalTime;
            this.shipIntervalTimeUnit = shipIntervalTimeUnit;
            return this;
        }

        public LogHouseConfiguration build() {
            return new LogHouseConfiguration(
                    applicationContext,
                    deliveryPerson,
                    gson,
                    logsPerRequest,
                    beforeShipFilter,
                    shipIntervalTime,
                    shipIntervalTimeUnit);
        }
    }
}
