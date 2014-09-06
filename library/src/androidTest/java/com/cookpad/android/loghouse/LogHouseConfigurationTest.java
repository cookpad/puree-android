package com.cookpad.android.loghouse;

import android.test.AndroidTestCase;

public class LogHouseConfigurationTest extends AndroidTestCase {

    public void testCheckDefaultValues() {
        LogHouseConfiguration conf = new LogHouseConfiguration.Builder(getContext())
                .build();
        assertNotNull(conf.getApplicationContext());
        assertNotNull(conf.getGson());
        assertNotNull(conf.getBeforeInsertAction());
    }
}
