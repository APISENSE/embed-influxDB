package io.apisense.embed.influx.download;

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
    private static final String PRODUCT_NAME = "influxdb";

    @Override
    public URL buildSource(VersionConfiguration configuration) {
        StringBuilder builder = new StringBuilder(HOSTNAME)
                .append("/").append(BASE_URL)
                .append("/").append(configuration.getVersion().getReleaseType().getDirectory())
                .append("/").append(PRODUCT_NAME)
                .append("-").append(configuration.getVersion().asInDownloadPath())
                .append("_").append(configuration.getOs().getDlPath())
                .append("_").append(configuration.getArchitecture().dlPath)
                .append(".").append(configuration.getOs().getArchiveType().getExtension());
        try {
            return new URL(builder.toString());
        } catch (MalformedURLException e) {
            logger.error("Unable to create valid download URL", e);
            throw new RuntimeException(e);
        }
    }
}
