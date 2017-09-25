package io.apisense.embed.influx.download;

import io.apisense.embed.influx.configuration.VersionConfiguration;

import java.io.File;
import java.net.URL;

interface UrlGenerator {
    /**
     * Generate influxDB download url depending on the given data.
     *
     * Note: Some OS/Architecture combination may not exist on InfluxDB website,
     * see: https://portal.influxdata.com/downloads
     *
     * @param configuration Server's version description.
     * @return The theoretical download url.
     */
    URL buildSource(VersionConfiguration configuration);

    /**
     * Generate the path to store the downloaded server.
     *
     * @param parent        The parent directory.
     * @param configuration Server's version description.
     * @return The extraction endpoint.
     */
    File buildTarget(File parent, VersionConfiguration configuration);
}
