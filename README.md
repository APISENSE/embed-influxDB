[![Travis](https://img.shields.io/travis/APISENSE/embed-influxDB.svg)]()
[![Codecov](https://img.shields.io/codecov/c/github/APISENSE/embed-influxDB.svg)]()


# Embedded InfluxDB

Start influx servers at runtime.

## Usage

```java
package io.apisense.embed.influx;

import io.apisense.embed.influx.configuration.InfluxConfigurationWriter;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ServerAlreadyRunningException, InterruptedException, ServerNotRunningException {
        InfluxServer.Builder builder = new InfluxServer.Builder();
        int freeHttpPort = Network.getFreeServerPort();
        int freeUdpPort = Network.getFreeServerPort();
        
        // configuration to start InfluxDB server with HTTP on port `freeHttpPort`
        // and default backup restore port
        InfluxConfigurationWriter configHttp = new InfluxConfigurationWriter(8088, freeHttpPort);
        
        // configuration to start InfluxDB server with HTTP on port `freeHttpPort`
        // and UDP enabled on `freeUdpPort` with default backup restore port.
        // UDP by default use database `udp`.
        
        // Customize the server configuration, namely used ports
        InfluxConfigurationWriter configHttpWithUdp = new InfluxConfigurationWriter(8088, freeHttpPort, freeUdpPort);
        
        builder.setInfluxConfiguration(configHttpWithUdp); // let's start both of protocols, HTTP and UDP
        InfluxServer server = builder.build();
        
        server.start();
        Thread.sleep(10 * 1000);
        server.stop();
    }
}
```

## Notes

- InfluxDB is using [2 ports by default per server](https://docs.influxdata.com/influxdb/v1.3/administration/ports/),
 one for the [Backup & Restore](https://docs.influxdata.com/influxdb/v1.3/administration/backup_and_restore/) feature,
 and another for the [HTTP query endpoint](https://docs.influxdata.com/influxdb/v1.3/tools/api/).
 But you can extend it with 3th port parameter for [UDP](https://github.com/influxdata/influxdb/blob/master/services/udp/README.md)
