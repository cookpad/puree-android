package com.cookpad.puree.storage;

import com.google.gson.JsonObject;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

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
            records.add(new Record(0, "logcat", new JsonObject()));
            assertThat(records.getIdsAsString(), is("0"));
        }
        {
            Records records = new Records();
            for (int i = 0; i < 3; i++) {
                records.add(new Record(i, "logcat", new JsonObject()));
            }
            assertThat(records.getIdsAsString(), is("0,1,2"));
        }
    }

    @Test
    public void getJsonLogs() {
        {
            Records records = new Records();
            assertThat(records.getJsonLogs().size(), Matchers.is(0));
        }
        {
            Records records = new Records();
            for (int i = 0; i < 3; i++) {
                records.add(new Record(i, "logcat", new JsonObject()));
            }
            assertThat(records.getJsonLogs().size(), is(3));
        }
    }

}
