package io.apisense.embed.influx.download;

import io.apisense.embed.influx.configuration.InfluxIVersion;
import io.apisense.embed.influx.configuration.VersionConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Default implementation of an {@link UrlGenerator}.
 */
public class InfluxUrlGenerator implements UrlGenerator {
    private static final Logger logger = LoggerFactory.getLogger(InfluxUrlGenerator.class.getName());
    public static final String HOSTNAME = "https://dl.influxdata.com";
    public static final String BASE_URL = "influxdb";

    private static final String PRODUCT_NAME_V1 = "influxdb";
    private static final String PRODUCT_NAME_V2 = "influxdb2";

    private boolean isV2(InfluxIVersion version) {
        return version.asInDownloadPath().startsWith("2");
    }

    private String versionSeparator(InfluxIVersion version) {
        return isV2(version) ? "-" : "_";
    }

    /**
     * Generates the URL to download versions from.
     * There are some slight changes between Influx v1 and v2 to consider.
     * <p>
     * v1 Linux:   <a href="https://dl.influxdata.com/influxdb/releases/influxdb-1.8.10_linux_amd64.tar.gz"/>
     * v1 Windows: <a href="https://dl.influxdata.com/influxdb/releases/influxdb-1.8.10_windows_amd64.zip"/>
     * v2 Linux:   <a href="https://dl.influxdata.com/influxdb/releases/influxdb2-2.5.1-linux-arm64.tar.gz"/>
     * v2 Windows: <a href="https://dl.influxdata.com/influxdb/releases/influxdb2-2.5.1-windows-amd64.zip"/>
     *
     * @param configuration Server's version description.
     * @return The final URL to download the Influx package.
     */
    @Override
    public URL buildSource(VersionConfiguration configuration) {
        StringBuilder builder = new StringBuilder(HOSTNAME)
                .append("/").append(BASE_URL)
                .append("/").append(configuration.getVersion().getReleaseType().getDirectory())
                .append("/").append(isV2(configuration.getVersion()) ? PRODUCT_NAME_V2 : PRODUCT_NAME_V1)
                .append("-").append(configuration.getVersion().asInDownloadPath())

                .append(versionSeparator(configuration.getVersion()))
                .append(configuration.getOs().getDlPath())

                .append(versionSeparator(configuration.getVersion()))
                .append(configuration.getArchitecture().getDlPath())

                .append(".").append(configuration.getOs().getArchiveType().getExtension());
        try {
            return new URL(builder.toString());
        } catch (MalformedURLException e) {
            logger.error("Unable to create valid download URL", e);
            throw new RuntimeException(e);
        }
    }
}
