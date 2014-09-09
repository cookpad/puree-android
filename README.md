LogHouse
====

![](http://upload.wikimedia.org/wikipedia/commons/thumb/4/47/Pfarr_Log_House.jpg/800px-Pfarr_Log_House.jpg)

## Description

Sending logs from mobile app is difficult, isn't it?

It is inefficient that send logs every time events are fired. So you should implement buffering, retrying, validation, ...
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
    @Override
    public void onCreate() {
        LogHouse.initialize(buildConfiguration(this));
    }

    public static LogHouseConfiguration buildConfiguration(Context context) {
        return new LogHouseConfiguration.Builder(context)
                .beforeEmitAction(new AddRequiredParamsAction())
                .registerOutput(OutBufferedLogcat.class)
                .registerOutput(OutLogcat.class)
                .build();
    }
}
```

### Sending logs

Log class should implements Log interface, and define type of output plugin.

```java
public class ClickLog extends Log {
    @SerializedName("page")
    private String page;
    @SerializedName("label")
    private String label;

    public String type() {
        return OutBufferedLogcat.TYPE;
    }

    public ClickLog(String page, String label) {
        this.page = page;
        this.label = label;
    }
}
```

Shed log to LogHouse in an arbitrary timing.

```java
LogHouse.in(new ClickLog("MainActivity", "Hello"));
```

### Testing

LogSpec provides utilities for tests.

```java
public class ClickLogTest extends AndroidTestCase {
    public void testFormat() {
        new LogSpec(DemoApplication.buildConfiguration(getContext()))
                .logs(new ClickLog("MainActivity", "Hello"),
                        new ClickLog("MainActivity", "World"),
                        new PvLog("MainActivity"))
                .target(OutBufferedLogcat.TYPE)
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

### Create output plugins

There are two types of output plugins: Non-Buffered, Buffered.

- Non-Buffered output plugins do not buffer data and immediately write out results.
- Buffered output plugins store logs to local storage temporary.

You can create a plugin by inheriting LogHouse.Output or LogHouse.BufferedOutput. See example plugins below.

```java
public class OutLogcat extends LogHouseOutput {
    public String type() {
        return "logcat";
    }

    @Override
    public void emit(JSONObject serializedLog) {
        Log.d("OutLogcat", serializedLog.toString());
    }
}
```

```java
public class OutBufferedLogcat extends LogHouseBufferedOutput {
    public String type() {
        return "buffered_logcat";
    }

    @Override
    public void emit(List<JSONObject> serializedLogs, AsyncResult asyncResult) {
        JSONArray log = new JSONArray();
        for (JSONObject serializedLog : serializedLogs) {
            log.put(serializedLog);
        }
        Log.d("OutBufferedLogcat", log.toString());

        asyncResult.success();
    }
}
```

## Install


Clone this repository in your PC and compile with your project for now.
I'll upload LogHouse to maven central sooner or later.

```java
// settings.gradle
include ':app', ':..:LogHouse:library', ':..:LogHouse:plugins'

// app/build.gradle
compile project(':..:LogHouse:library')
compile project(':..:LogHouse:plugins')
```
