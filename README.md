[![Travis](https://img.shields.io/travis/APISENSE/embed-influxDB.svg)](https://travis-ci.org/APISENSE/embed-influxDB)
[![Codecov](https://img.shields.io/codecov/c/github/APISENSE/embed-influxDB.svg)](https://codecov.io/gh/APISENSE/embed-influxDB)
[![Maven Central](https://img.shields.io/maven-central/v/io.apisense.embed.influx/embed-influxDB.svg)](http://search.maven.org/#artifactdetails%7Cio.apisense.embed.influx%7Cembed-influxDB%7C1.0.0%7Cjar)

# Embedded InfluxDB

Start influx servers at runtime.

## Usage

```java
import de.flapdoodle.embed.process.runtime.Network;
import io.apisense.embed.influx.InfluxServer;
import io.apisense.embed.influx.ServerAlreadyRunningException;
import io.apisense.embed.influx.ServerNotRunningException;
import io.apisense.embed.influx.configuration.InfluxConfigurationWriter;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ServerAlreadyRunningException, InterruptedException, ServerNotRunningException {
        InfluxServer.Builder builder = new InfluxServer.Builder();
        // Customize the server configuration, namely used ports
        int freeHttpPort = Network.getFreeServerPort();
        builder.setInfluxConfiguration(new InfluxConfigurationWriter(8088, freeHttpPort));
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
