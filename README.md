LogHouse
====

![](http://upload.wikimedia.org/wikipedia/commons/thumb/4/47/Pfarr_Log_House.jpg/800px-Pfarr_Log_House.jpg)

LogHouse is accommodations that have buffering, retrying... and more functions

## Description

It is inefficient that send logs every time events are fired.
LogHouse provides some functions like below

- Buffering
 - Store logs on local storage until log was sent.
- Retrying
 - Retry to send log after some time automatically if sending has failed.
- Validation
 - Enable to receive broken logs.

and more.

## Usage

Initialize LogHouse on application created.

```java
public class DemoApplication extends Application {
    public static final String TAG = DemoApplication.class.getSimpleName();

    private DeliveryPerson deliveryPerson = new DeliveryPerson() {
        @Override
        public boolean onShip(List<String> serializedLogs) {
            // send logs to your server
            return true;
        }
    };

    private AroundShipFilter aroundShipFilter = new AroundShipFilter() {
        @Override
        public List<String> beforeShip(List<String> serializedLogs) {
            // set common params, filtering, etc...
            return serializedLogs;
        }

        @Override
        public void afterShip(List<String> serializedLogs) {
            // you can see sent logs
        }
    };

    @Override
    public void onCreate() {
        LogHouseConfiguration conf = new LogHouseConfiguration.Builder(this, deliveryPerson)
                .shipInterval(5, Calendar.MINUTE)
                .aroundShipFilter(aroundShipFilter)
                .build();
        LogHouseManager.initialize(conf);
    }
}
```

Log should implements Log interface.

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

## Install

Clone this repository in your PC and compile with your project for now.
I'll upload LogHouse to maven central sooner or later.

```java
// settings.gradle
include ':app', ':..:LogHouse:library'

// app/build.gradle
compile project(':..:LogHouse:library')
```
