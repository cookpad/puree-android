package com.example.loghouse.logs;

import android.test.AndroidTestCase;

import com.cookpad.android.loghouse.Log;
import com.cookpad.android.loghouse.LogHouseConfiguration;
import com.cookpad.android.loghouse.LogHouseManager;
import com.cookpad.android.loghouse.handlers.BeforeShipAction;
import com.cookpad.android.loghouse.handlers.DeliveryPerson;
import com.example.loghouse.AddRequiredParamsAction;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

public class ClickLogTest extends AndroidTestCase {

    private DeliveryPerson deliveryPerson = new DeliveryPerson() {
        @Override
        public boolean onShip(List<JSONObject> serializedLogs) {
            return true;
        }
    };

    public void testCheckFormat() {
        BeforeShipAction beforeShipAction = new BeforeShipAction() {
            @Override
            public List<JSONObject> beforeShip(List<JSONObject> serializedLogs) {
                assertEquals(1, serializedLogs.size());
                JSONObject serializedLog = serializedLogs.get(0);
                assertTrue(serializedLog.has("page"));
                assertTrue(serializedLog.has("label"));
                assertTrue(serializedLog.has("event_time"));
                return serializedLogs;
            }
        };

        LogHouseConfiguration conf = new LogHouseConfiguration.Builder(getContext(), deliveryPerson)
                .beforeInsertFilter(new AddRequiredParamsAction())
                .beforeShipFilter(beforeShipAction)
                .build();
        LogHouseManager.initialize(conf);

        Log log = new ClickLog("MainActivity", "Hello");
        LogHouseManager.insertSync(log.toJSON(new Gson()));
        LogHouseManager.shipSync(1);
    }
}
