package com.cookpad.puree;

import android.test.AndroidTestCase;

import com.cookpad.puree.PureeConfiguration;

public class PureeConfigurationTest extends AndroidTestCase {

    public void testCheckDefaultValues() {
        PureeConfiguration conf = new PureeConfiguration.Builder(getContext())
                .build();
        assertNotNull(conf.getApplicationContext());
        assertNotNull(conf.getGson());
    }
}
