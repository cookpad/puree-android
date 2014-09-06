package com.cookpad.android.loghouse;

import android.test.AndroidTestCase;

public class LogHouseConfigurationTest extends AndroidTestCase {

    public void checkDefaultValues() {
        LogHouseConfiguration conf = new LogHouseConfiguration.Builder(getContext())
                .build();
        assertNotNull(conf.getApplicationContext());
        assertNotNull(conf.getGson());
        assertEquals(1000, conf.getLogsPerRequest());
        assertNull(conf.getBeforeInsertAction());
        assertNull(conf.getBeforeShipAction());
    }
}
