[![Travis](https://img.shields.io/travis/APISENSE/embed-influxDB.svg)](https://travis-ci.org/APISENSE/embed-influxDB)
[![Codecov](https://img.shields.io/codecov/c/github/APISENSE/embed-influxDB.svg)](https://codecov.io/gh/APISENSE/embed-influxDB)
[![Maven Central](https://img.shields.io/maven-central/v/io.apisense.embed.influx/embed-influxDB.svg)](http://search.maven.org/#artifactdetails%7Cio.apisense.embed.influx%7Cembed-influxDB%7C1.0.0%7Cjar)
[![Code Climate](https://img.shields.io/codeclimate/issues/github/APISENSE/embed-influxDB.svg)](https://codeclimate.com/github/APISENSE/embed-influxDB)
[![Code Climate](https://img.shields.io/codeclimate/maintainability/APISENSE/embed-influxDB.svg)](https://codeclimate.com/github/APISENSE/embed-influxDB)

# Embedded InfluxDB

Start influx servers at runtime.

## Usage

```java
import de.flapdoodle.embed.process.runtime.Network;
import io.apisense.embed.influx.InfluxServer;
import io.apisense.embed.influx.ServerAlreadyRunningException;
import io.apisense.embed.influx.ServerNotRunningException;
import io.apisense.embed.influx.configuration.CustomInfluxVersion;
import io.apisense.embed.influx.configuration.InfluxConfigurationWriter;
import io.apisense.embed.influx.configuration.InfluxVersion;
import io.apisense.embed.influx.configuration.OSArchitecture;
import io.apisense.embed.influx.configuration.OSType;
import io.apisense.embed.influx.configuration.VersionConfiguration;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ServerAlreadyRunningException, InterruptedException, ServerNotRunningException {
        InfluxServer.Builder builder = new InfluxServer.Builder();
        int freeHttpPort = Network.getFreeServerPort();
        int freeUdpPort = Network.getFreeServerPort();

        // configuration to start InfluxDB server with HTTP on port `freeHttpPort`
        // and default backup restore port
        InfluxConfigurationWriter influxConfig = new InfluxConfigurationWriter.Builder()
                .setHttp(freeHttpPort) // by default auth is disabled
                .setUdp(freeUdpPort) // If you happen to need udp enabled, by default to 'udp' database
                .build();

        builder.setInfluxConfiguration(influxConfig); // let's start both of protocols, HTTP and UDP

        // You can either autodetect the Runtime environment to use, or fully customize your distribution.
        // Using an already integrated or a custom version number.
        builder.setVersionConfig(VersionConfiguration.fromRuntime(new CustomInfluxVersion("2.5.1")));
        //  builder.setVersionConfig(new VersionConfiguration(OSType.LINUX, OSArchitecture.x86_64, InfluxVersion.2_5_1));

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
  But you can extend it with 3th port parameter
  for [UDP](https://github.com/influxdata/influxdb/blob/master/services/udp/README.md)
