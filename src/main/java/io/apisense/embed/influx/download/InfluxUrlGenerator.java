package io.apisense.embed.influx.download;

import io.apisense.embed.influx.configuration.VersionConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Default implementation of an {@link UrlGenerator}.
 */
class InfluxUrlGenerator implements UrlGenerator {
    private static final Logger logger = LoggerFactory.getLogger(InfluxUrlGenerator.class.getName());
    private static final String BASE_URL = "https://dl.influxdata.com/influxdb";
    private static final String PRODUCT_NAME = "influxdb";

    @Override
    public URL buildSource(VersionConfiguration configuration) {
        StringBuilder builder = new StringBuilder(BASE_URL)
                .append("/").append(configuration.version.directory)
                .append("/").append(PRODUCT_NAME)
                .append("-").append(configuration.version.dlPath)
                .append("_").append(configuration.os.dlPath)
                .append("_").append(configuration.architecture.dlPath)
                .append(".").append(configuration.os.archiveType.extension);
        try {
            return new URL(builder.toString());
        } catch (MalformedURLException e) {
            logger.error("Unable to create valid download URL", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public File buildTarget(File parent, VersionConfiguration configuration) {
        StringBuilder builder = new StringBuilder(PRODUCT_NAME)
                .append("-").append(configuration.version.dlPath)
                .append("_").append(configuration.os.dlPath)
                .append("_").append(configuration.architecture.dlPath);
        return new File(parent, builder.toString());
    }
}
