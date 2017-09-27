[![Travis](https://img.shields.io/travis/APISENSE/embed-influxDB.svg)]()
[![Codecov](https://img.shields.io/codecov/c/github/APISENSE/embed-influxDB.svg)]()


# Embedded InfluxDB

Start influx servers at runtime.

Usage:

```java
class Main {
    public static void main(String[] args){
      InfluxServer server = new InfluxServer.Builder().build();
      
      server.start();
      // [...]
      server.cleanup();
    }
}
```