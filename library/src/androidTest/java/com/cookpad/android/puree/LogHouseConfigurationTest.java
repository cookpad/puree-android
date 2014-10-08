package com.cookpad.android.puree;

import android.test.AndroidTestCase;

import com.cookpad.android.puree.LogHouseConfiguration;

public class LogHouseConfigurationTest extends AndroidTestCase {

    public void testCheckDefaultValues() {
        LogHouseConfiguration conf = new LogHouseConfiguration.Builder(getContext())
                .build();
        assertNotNull(conf.getApplicationContext());
        assertNotNull(conf.getGson());
        assertNotNull(conf.getBeforeEmitFilter());
    }
}
