package com.cookpad.puree.storage;

import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class RecordsTest {
    @Test
    public void getIdsAsString() {
        {
            Records records = new Records();
            assertThat(records.getIdsAsString(), is(""));
        }
        {
            Records records = new Records();
            records.add(new Record(0, "logcat", new JSONObject()));
            assertThat(records.getIdsAsString(), is("0"));
        }
        {
            Records records = new Records();
            for (int i = 0; i < 3; i++) {
                records.add(new Record(i, "logcat", new JSONObject()));
            }
            assertThat(records.getIdsAsString(), is("0,1,2"));
        }
    }

    @Test
    public void getJsonLogs() {
        {
            Records records = new Records();
            assertThat(records.getJsonLogs().length(), Matchers.is(0));
        }
        {
            Records records = new Records();
            for (int i = 0; i < 3; i++) {
                records.add(new Record(i, "logcat", new JSONObject()));
            }
            assertThat(records.getJsonLogs().length(), is(3));
        }
    }
}
