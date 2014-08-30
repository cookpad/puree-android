package com.cookpad.android.loghouse;

import android.content.Context;

import com.google.gson.Gson;

import java.util.Calendar;

public class LogHouseConfiguration {
    private Context applicationContext;
    private DeliveryPerson deliveryPerson;
    private Gson gson;
    private int shipIntervalTime;
    private int shipIntervalTimeUnit;
    private AroundShipFilter aroundShipFilter;

    public Context getApplicationContext() {
        return applicationContext;
    }

    public DeliveryPerson getDeliveryPerson() {
        return deliveryPerson;
    }

    public Gson getGson() {
        return gson;
    }

    public int getShipIntervalTime() {
        return shipIntervalTime;
    }

    public int getShipIntervalTimeUnit() {
        return shipIntervalTimeUnit;
    }

    public AroundShipFilter getAroundShipFilter() {
        return aroundShipFilter;
    }

    public LogHouseConfiguration(Context applicationContext, DeliveryPerson deliveryPerson,
                                 Gson gson, AroundShipFilter aroundShipFilter,
                                 int shipIntervalTime, int shipIntervalTimeUnit) {
        this.applicationContext = applicationContext;
        this.deliveryPerson = deliveryPerson;
        this.gson = gson;
        this.shipIntervalTime = shipIntervalTime;
        this.shipIntervalTimeUnit = shipIntervalTimeUnit;
        this.aroundShipFilter = aroundShipFilter;
    }

    public static class Builder {
        private Context applicationContext;
        private DeliveryPerson deliveryPerson;
        private Gson gson = new Gson();
        private AroundShipFilter aroundShipFilter = new AroundShipFilter();
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

        public Builder aroundShipFilter(AroundShipFilter aroundShipFilter) {
            this.aroundShipFilter = aroundShipFilter;
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
                    aroundShipFilter,
                    shipIntervalTime,
                    shipIntervalTimeUnit);
        }
    }
}
