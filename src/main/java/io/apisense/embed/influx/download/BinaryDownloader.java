package io.apisense.embed.influx.download;

import io.apisense.embed.influx.configuration.VersionConfiguration;

import java.io.File;
import java.io.IOException;

public interface BinaryDownloader {

    /**
     * Download the right version of InfluxDB if necessary,
     * then return a {@link File} referencing the server binary.
     *
     * @param configuration Server's version description.
     * @return The server binary.
     */
    File download(VersionConfiguration configuration) throws IOException;
}
