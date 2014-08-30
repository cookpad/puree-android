package com.cookpad.android.loghouse;

import android.content.Context;

import com.google.gson.Gson;

public class LogHouseConfiguration {
    private Context applicationContext;
    private Gson gson;

    public Context getApplicationContext() {
        return applicationContext;
    }

    public Gson getGson() {
        return gson;
    }

    public LogHouseConfiguration(Context applicationContext, Gson gson) {
        this.applicationContext = applicationContext;
        this.gson = gson;
    }

    public static class Builder {
        private Context applicationContext;
        private Gson gson = new Gson();

        public Builder(Context applicationContext) {
            this.applicationContext = applicationContext;
        }

        public Builder gson(Gson gson) {
            this.gson = gson;
            return this;
        }

        public LogHouseConfiguration build() {
            return new LogHouseConfiguration(
                    applicationContext,
                    gson);
        }
    }
}
