package io.apisense.embed.influx.download;

import io.apisense.embed.influx.configuration.VersionConfiguration;

import java.net.URL;

/**
 * Operations used for generating a download URL complying with InfluxDB API,
 * and a common output directory definition depending on the downloaded version.
 */
interface UrlGenerator {
    /**
     * Generate influxDB download url depending on the given data.
     * <p>
     * Note: Some OS/Architecture combination may not exist on InfluxDB website,
     * see: <a href="https://portal.influxdata.com/downloads">https://portal.influxdata.com/downloads</a>
     *
     * @param configuration Server's version description.
     * @return The theoretical download url.
     */
    URL buildSource(VersionConfiguration configuration);
}
