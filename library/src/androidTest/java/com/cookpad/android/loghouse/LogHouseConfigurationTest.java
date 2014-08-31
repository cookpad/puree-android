package com.cookpad.android.loghouse;

import android.test.AndroidTestCase;

import com.cookpad.android.loghouse.handlers.DeliveryPerson;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

public class LogHouseConfigurationTest extends AndroidTestCase {

    public void checkDefaultValues() {
        DeliveryPerson deliveryPerson = new DeliveryPerson() {
            @Override
            public boolean onShip(List<JSONObject> serializedLogs) {
                // do nothing
                return true;
            }
        };
        LogHouseConfiguration conf = new LogHouseConfiguration.Builder(getContext(), deliveryPerson)
                .build();
        assertNotNull(conf.getApplicationContext());
        assertNotNull(conf.getDeliveryPerson());
        assertNotNull(conf.getGson());
        assertEquals(1000, conf.getLogsPerRequest());
        assertEquals(5, conf.getShipIntervalTime());
        assertEquals(Calendar.SECOND, conf.getShipIntervalTimeUnit());
        assertNotNull(conf.getAroundShipFilter());
    }
}
