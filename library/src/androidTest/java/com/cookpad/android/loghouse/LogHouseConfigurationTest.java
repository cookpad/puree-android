package com.cookpad.android.loghouse;

import android.test.AndroidTestCase;

import com.cookpad.android.loghouse.handlers.DeliveryPerson;

import java.util.Calendar;
import java.util.List;

public class LogHouseConfigurationTest extends AndroidTestCase {

    public void checkDefaultValues() {
        DeliveryPerson deliveryPerson = new DeliveryPerson() {
            @Override
            public void onShip(List<String> serializedLogs) {
                // do nothing
            }
        };
        LogHouseConfiguration conf = new LogHouseConfiguration.Builder(getContext(), deliveryPerson)
                .build();
        assertNotNull(conf.getApplicationContext());
        assertNotNull(conf.getDeliveryPerson());
        assertNotNull(conf.getGson());
        assertEquals(5, conf.getShipIntervalTime());
        assertEquals(Calendar.SECOND, conf.getShipIntervalTimeUnit());
        assertNotNull(conf.getAroundShipFilter());
    }
}
