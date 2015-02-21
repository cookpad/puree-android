package com.cookpad.puree;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class KeyTest {
    @Test
    public void getId() {
        {
            Key key = Key.from(FooLog.class);
            assertThat(key.getId(), is("com.cookpad.puree.KeyTest$FooLog"));
        }
        {
            Key key = Key.from(BarLog.class);
            assertThat(key.getId(), is("com.cookpad.puree.KeyTest$BarLog"));
        }
    }

    @Test
    public void equals() {
        {
            Key key1 = Key.from(FooLog.class);
            Key key2 = Key.from(BarLog.class);
            assertThat(key1.equals(key2), is(false));
        }
        {
            Key key1 = Key.from(FooLog.class);
            Key key2 = Key.from(FooLog.class);
            assertThat(key1.equals(key2), is(true));
        }
    }

    private static class FooLog extends JsonConvertible {
    }

    private static class BarLog extends JsonConvertible {
    }
}
