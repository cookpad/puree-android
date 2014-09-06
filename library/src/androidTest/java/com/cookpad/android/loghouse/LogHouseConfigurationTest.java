package com.cookpad.android.loghouse;

import android.test.AndroidTestCase;

import java.util.Calendar;

public class LogHouseConfigurationTest extends AndroidTestCase {

    public void checkDefaultValues() {
        LogHouseConfiguration conf = new LogHouseConfiguration.Builder(getContext())
                .build();
        assertNotNull(conf.getApplicationContext());
        assertNotNull(conf.getGson());
        assertEquals(1000, conf.getLogsPerRequest());
        assertEquals(5, conf.getShipIntervalTime());
        assertEquals(Calendar.SECOND, conf.getShipIntervalTimeUnit());
        assertNull(conf.getBeforeInsertAction());
        assertNull(conf.getBeforeShipAction());
    }
}
