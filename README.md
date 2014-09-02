LogHouse
====

![](http://upload.wikimedia.org/wikipedia/commons/thumb/4/47/Pfarr_Log_House.jpg/800px-Pfarr_Log_House.jpg)

## Description

**Sending logs from mobile app is difficult, isn't it?**

It is inefficient that send logs every time events are fired. So you should implement buffering, retrying, validation, ... and more functions.

LogHouse is a data collector for unified logging layer, which provides some functions like below

- Buffering
 - Store logs on local storage until log was sent.
- Retrying
 - Retry to send log after some time automatically if sending has failed.
- Validation
 - Enable to receive broken logs.

## Usage

### Initializing

Configure LogHouse on application created.

```java
public class DemoApplication extends Application {
    private DeliveryPerson deliveryPerson = new DeliveryPerson() {
        @Override
        public boolean onShip(List<JSONObject> serializedLogs) {
            // send logs to your server
            return true;
        }
    };

    private BeforeInsertAction beforeInsertAction = new BeforeInsertAction() {
        @Override
        public JSONObject call(JSONObject serializedLog) {
            // set common params, filtering, etc...
            return serializedLog;
        }
    };

    @Override
    public void onCreate() {
        LogHouseConfiguration conf = new LogHouseConfiguration.Builder(this, deliveryPerson)
                .logsPerRequest(3)
                .shipInterval(3, Calendar.SECOND)
                .beforeInsertAction(beforeInsertAction)
                .build();
        LogHouseManager.initialize(conf);
}
```

### Sending logs

Log class should implements Log interface.

```java
public class ClickLog implements Log {
    private String page;
    private String label;

    public ClickLog(String page, String label) {
        this.page = page;
        this.label = label;
    }
}
```

Ask to LogHouseManager for sending log.

```java
LogHouseManager.ask(new ClickLog("MainActivity", "Hello"));
```

DeliveryPerson comes after a while.

### Testing

LogSpec provides utilities for tests.

```java
public class ClickLogTest extends AndroidTestCase {
    public void testCheckFormat() {
        new LogSpec(getContext())
                .beforeInsertAction(new AddRequiredParamsAction())
                .logs(new ClickLog("MainActivity", "Hello"), new ClickLog("MainActivity", "World"))
                .shouldBe(new LogSpec.Matcher() {
                    @Override
                    public void expect(List<JSONObject> serializedLogs) throws JSONException {
                        assertEquals(2, serializedLogs.size());
                        JSONObject serializedLog = serializedLogs.get(0);
                        assertEquals("MainActivity", serializedLog.getString("page"));
                        assertEquals("Hello", serializedLog.getString("label"));
                        assertTrue(serializedLog.has("event_time"));
                    }
                });
    }
}
```

## Install

Clone this repository in your PC and compile with your project for now.
I'll upload LogHouse to maven central sooner or later.

```java
// settings.gradle
include ':app', ':..:LogHouse:library'

// app/build.gradle
compile project(':..:LogHouse:library')
```
