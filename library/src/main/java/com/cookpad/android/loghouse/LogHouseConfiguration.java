package com.cookpad.android.loghouse;

import android.content.Context;

import com.google.gson.Gson;

public class LogHouseConfiguration {
    private Context applicationContext;
    private Gson gson;
    private AroundShipFilter aroundShipFilter;

    public Context getApplicationContext() {
        return applicationContext;
    }

    public Gson getGson() {
        return gson;
    }

    public AroundShipFilter getAroundShipFilter() {
        return aroundShipFilter;
    }

    public LogHouseConfiguration(Context applicationContext, Gson gson,
                                 AroundShipFilter aroundShipFilter) {
        this.applicationContext = applicationContext;
        this.gson = gson;
        this.aroundShipFilter = aroundShipFilter;
    }

    public static class Builder {
        private Context applicationContext;
        private Gson gson = new Gson();
        private AroundShipFilter aroundShipFilter = new AroundShipFilter();

        public Builder(Context applicationContext) {
            this.applicationContext = applicationContext;
        }

        public Builder gson(Gson gson) {
            this.gson = gson;
            return this;
        }

        public Builder aroundShipFilter(AroundShipFilter aroundShipFilter) {
            this.aroundShipFilter = aroundShipFilter;
            return this;
        }

        public LogHouseConfiguration build() {
            return new LogHouseConfiguration(
                    applicationContext,
                    gson,
                    aroundShipFilter);
        }
    }
}
