package com.cookpad.android.loghouse;

import android.content.Context;

public class LogHouseConfiguration {
    private Context applicationContext;

    public LogHouseConfiguration(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public static class Builder {
        private Context applicationContext;

        public Builder(Context applicationContext) {
            this.applicationContext = applicationContext;
        }

        public LogHouseConfiguration build() {
            return new LogHouseConfiguration(applicationContext);
        }
    }
}
