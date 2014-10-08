package com.cookpad.android.puree;

import android.test.AndroidTestCase;

public class PureeConfigurationTest extends AndroidTestCase {

    public void testCheckDefaultValues() {
        PureeConfiguration conf = new PureeConfiguration.Builder(getContext())
                .build();
        assertNotNull(conf.getApplicationContext());
        assertNotNull(conf.getGson());
        assertNotNull(conf.getBeforeEmitFilter());
    }
}
