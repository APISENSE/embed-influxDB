[![Travis](https://img.shields.io/travis/APISENSE/embed-influxDB.svg)]()
[![Codecov](https://img.shields.io/codecov/c/github/APISENSE/embed-influxDB.svg)]()


# Embedded InfluxDB

Start influx servers at runtime.

Usage:

```java
package io.apisense.embed.influx;

import io.apisense.embed.influx.configuration.InfluxConfigurationWriter;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ServerAlreadyRunningException, InterruptedException, ServerNotRunningException {
        InfluxServer.Builder builder = new InfluxServer.Builder();
        builder.setInfluxConfiguration(new InfluxConfigurationWriter(9966)); // Assign a new port
        InfluxServer server = builder.build();
        
        server.start();
        Thread.sleep(10 * 1000);
        server.stop();
    }
}
```